package com.escandallos.ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Componente visual JavaFX para mostrar métricas KPI con estilos CSS personalizados.
 */
public class MetricCard extends VBox {
    private final Label titleLabel;
    private final Label valueLabel;
    private final Label subtitleLabel;

    public MetricCard(String title, String initialValue, String subtitle, String accentColorHex) {
        getStyleClass().add("metric-card");
        setSpacing(4);
        setPadding(new Insets(16, 20, 16, 20));

        // Color Bar Indicator
        Region colorBar = new Region();
        colorBar.setPrefWidth(4);
        colorBar.setPrefHeight(16);
        colorBar.setStyle("-fx-background-color: " + accentColorHex + "; -fx-background-radius: 2px;");

        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("metric-card-title");

        HBox header = new HBox(8, colorBar, titleLabel);

        valueLabel = new Label(initialValue);
        valueLabel.getStyleClass().add("metric-card-value");

        subtitleLabel = new Label(subtitle);
        subtitleLabel.getStyleClass().add("metric-card-subtitle");

        getChildren().addAll(header, valueLabel, subtitleLabel);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }

    public void setSubtitle(String subtitle) {
        subtitleLabel.setText(subtitle);
    }
}
