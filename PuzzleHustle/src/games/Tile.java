package games;

import java.util.HashMap;

import javafx.geometry.Pos;
import javafx.scene.control.Label;

public class Tile {
	private Label label;
	private int value;
	private String valueStr;
	private String color = "-fx-background-color: #fff;";
	private String fontSize = "-fx-font-size: 50px;";
	private String fontWeight = "-fx-font-weight: bold;" + "-fx-border-radius: 10 10 10 10;"
  	+"-fx-background-radius: 10 10 10 10;";
	private HashMap <String, String> colorMap;
	
	public Tile() {
		valueStr = "";
		label = new Label(valueStr);
		label.setMinSize(120, 120);
		label.setAlignment(Pos.CENTER);
		label.setStyle(color + fontSize + fontWeight + "-fx-text-fill: #fff;");
		colorMap = initColorMap();
	}
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
		valueStr = "" + value;
		color = colorMap.get(valueStr);
		getLabel().setStyle(color);
		if(value == 0) {
			valueStr = "";
		}
		getLabel().setText(valueStr);
	}
	private HashMap<String,String> initColorMap() {
		HashMap<String,String> returnMap = new HashMap<>();
		returnMap.put("" + 0, "-fx-background-color: #fff;" + fontSize + fontWeight);
		returnMap.put("" + 2, "-fx-background-color: #ede1c9;" + fontSize + fontWeight+ "-fx-text-fill: #776d64;");
		returnMap.put("" + 4, "-fx-background-color: #ede1c9;"+ fontSize + fontWeight+ "-fx-text-fill: #776d64;");
		returnMap.put("" + 8, "-fx-background-color: #f2b179;"+ fontSize + fontWeight+ "-fx-text-fill: #f6ead4;");
		returnMap.put("" + 16, "-fx-background-color: #f49663;"+ fontSize + fontWeight+ "-fx-text-fill: #f6ead4;");
		returnMap.put("" + 32, "-fx-background-color: #f47d5f;"+ fontSize + fontWeight+ "-fx-text-fill: #f6ead4;");
		returnMap.put("" + 64, "-fx-background-color: #f75e3e;"+ fontSize + fontWeight+ "-fx-text-fill: #f6ead4;");
		returnMap.put("" + 128, "-fx-background-color: #ebce70;"+ fontSize + fontWeight+ "-fx-text-fill: #f6ead4;");
		returnMap.put("" + 256, "-fx-background-color: #eccb60;"+ fontSize + fontWeight+ "-fx-text-fill: #f6ead4;");
		returnMap.put("" + 512, "-fx-background-color: #eac74f;"+ fontSize + fontWeight+ "-fx-text-fill: #f6ead4;");
		returnMap.put("" + 1024, "-fx-background-color: #ebc540;"+ "-fx-font-size: 40px;" + fontWeight);
		returnMap.put("" + 2048, "-fx-background-color: #eec02f;"+ "-fx-font-size: 40px;" + fontWeight);
		returnMap.put("" + 4096, "-fx-background-color: #ee676b;"+ "-fx-font-size: 40px;" + "-fx-text-fill: #f6ead4;" + fontWeight);
		returnMap.put("" + 8192, "-fx-background-color: #ed4c5b;"+ "-fx-font-size: 40px;" + "-fx-text-fill: #f6ead4;" + fontWeight);
		returnMap.put("" + 16384, "-fx-background-color: #1882cc;"+ "-fx-font-size: 30px;" + "-fx-text-fill: #f6ead4;" + fontWeight);
		returnMap.put("" + 32768, "-fx-background-color: #73b4d2;"+ "-fx-font-size: 30px;" + "-fx-text-fill: #f6ead4;" + fontWeight);
		return returnMap;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tile [label=").append(label).append(", value=").append(value).append("]");
		return builder.toString();
	}
}
