package id;

import model.XmlDocument;
import model.XmlElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Налага правилата за уникални id върху документ. */
public class UniqueIdAssigner {
    /** Присвоява уникални id-та на всички елементи. */
    public void assign(XmlDocument doc) {
        List<XmlElement> all = new ArrayList<>();
        walk(doc.getRoot(), all);

        Map<String, List<XmlElement>> grouped = new LinkedHashMap<>();
        for (XmlElement el : all) {
            String origId = el.getAttributes().get("id");
            grouped.computeIfAbsent(origId, k -> new ArrayList<>()).add(el);
        }

        Set<String> takenIds = new HashSet<>();
        for (var entry : grouped.entrySet()) {
            String origId = entry.getKey();
            List<XmlElement> group = entry.getValue();
            if (origId == null) continue;
            if (group.size() == 1) takenIds.add(origId);
        }

        int genCounter = 0;
        Map<String, Integer> dupCounters = new HashMap<>();
        for (XmlElement el : all) {
            String orig = el.getAttributes().get("id");
            String finalId;
            if (orig == null) {
                do {
                    finalId = "_gen_" + (++genCounter);
                } while (takenIds.contains(finalId));
            } else if (grouped.get(orig).size() == 1) {
                finalId = orig;
            } else {
                int n;
                do {
                    n = dupCounters.merge(orig, 1, Integer::sum);
                    finalId = orig + "_" + n;
                } while (takenIds.contains(finalId));
            }
            takenIds.add(finalId);
            el.setId(finalId);
            el.getAttributes().put("id", finalId);
            doc.registerId(finalId, el);
        }
    }

    private void walk(XmlElement el, List<XmlElement> out) {
        out.add(el);
        for (XmlElement c : el.getChildren()) walk(c, out);
    }
}
