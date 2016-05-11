package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlUtils {
	/**
	 * XML生成
	 * 
	 * @param maps
	 * @return
	 */
	public static String createXml(List<Map<String, String>> maps, String rootnext) {
		String result = null;
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		for (Map<String, String> map : maps) {
			Element collage = root.addElement(rootnext);
			Set<String> set = map.keySet();
			Object[] keyArray = set.toArray();
			for (Object key : keyArray) {
				if (key != null && map.get(key) != null) {
					Element e = collage.addElement(key.toString());
					e.setText(map.get(key));
				}
			}

		}
		result = document.asXML();
		return result;
	}

	public static void main(String[] args) {

		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		for (int i = 0; i < 5; i++) {
			Map<String, String> m = new HashMap<String, String>();
			m.put("q", "qq" + i);
			m.put("w", "ww" + i);
			m.put("e", "ee" + i);
			maps.add(m);
		}
		System.out.println(createXml(maps, "qwert"));

	}
}
