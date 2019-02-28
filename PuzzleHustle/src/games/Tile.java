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
	private String fontWeight = "-fx-font-weight: bold;";
	private HashMap <String, String> colorMap;
	
	public Tile() {
		valueStr = "";
		label = new Label(valueStr);
		label.setMinSize(140, 140);
		label.setAlignment(Pos.CENTER);
		label.setStyle(color + fontSize + fontWeight);
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
		returnMap.put("" + 2, "-fx-background-color: #fffac8;" + fontSize + fontWeight);
		returnMap.put("" + 4, "-fx-background-color: #ffd8b1;"+ fontSize + fontWeight);
		returnMap.put("" + 8, "-fx-background-color: #fabebe;"+ fontSize + fontWeight);
		returnMap.put("" + 16, "-fx-background-color: #ffe119;"+ fontSize + fontWeight);
		returnMap.put("" + 32, "-fx-background-color: #f58231;"+ fontSize + fontWeight);
		returnMap.put("" + 64, "-fx-background-color: #e6194B;"+ fontSize + fontWeight);
		returnMap.put("" + 128, "-fx-background-color: #f032e6;"+ fontSize + fontWeight);
		returnMap.put("" + 256, "-fx-background-color: #a0a2f6;"+ fontSize + fontWeight);
		returnMap.put("" + 512, "-fx-background-color: #4363d8;"+ fontSize + fontWeight);
		returnMap.put("" + 1024, "-fx-background-color: #000075;"+ "-fx-font-size: 40px;" + fontWeight);
		returnMap.put("" + 2048, "-fx-background-color: #469990;"+ "-fx-font-size: 40px;" + fontWeight);
		returnMap.put("" + 4096, "-fx-background-color: #333;"+ "-fx-font-size: 40px;" + "-fx-text-fill: #fff;" + fontWeight);
		returnMap.put("" + 8192, "-fx-background-color: #222;"+ "-fx-font-size: 40px;" + "-fx-text-fill: #fff;" + fontWeight);
		returnMap.put("" + 16384, "-fx-background-color: #111;"+ "-fx-font-size: 30px;" + "-fx-text-fill: #fff;" + fontWeight);
		returnMap.put("" + 32768, "-fx-background-color: #000;"+ "-fx-font-size: 30px;" + "-fx-text-fill: #fff;" + fontWeight);
		return returnMap;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tile [label=").append(label).append(", value=").append(value).append("]");
		return builder.toString();
	}
}
