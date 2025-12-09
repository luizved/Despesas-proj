package org.despesas.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Despesa {
    private final IntegerProperty id;
    private final StringProperty descricao;
    private final DoubleProperty valor;
    private final ObjectProperty<LocalDate> data;
    private final StringProperty categoria;

    // Construtor para criar uma nova despesa (sem ID, gerado pelo BD)
    public Despesa(String descricao, double valor, LocalDate data, String categoria) {
        this.id = new SimpleIntegerProperty(0);
        this.descricao = new SimpleStringProperty(descricao);
        this.valor = new SimpleDoubleProperty(valor);
        this.data = new SimpleObjectProperty<>(data);
        this.categoria = new SimpleStringProperty(categoria);
    }

    // Construtor para carregar despesas do banco de dados (com ID)
    public Despesa(int id, String descricao, double valor, LocalDate data, String categoria) {
        this.id = new SimpleIntegerProperty(id);
        this.descricao = new SimpleStringProperty(descricao);
        this.valor = new SimpleDoubleProperty(valor);
        this.data = new SimpleObjectProperty<>(data);
        this.categoria = new SimpleStringProperty(categoria);
    }

    // Getters para as propriedades (necessário para TableView)
    public IntegerProperty idProperty() { return id; }
    public StringProperty descricaoProperty() { return descricao; }
    public DoubleProperty valorProperty() { return valor; }
    public ObjectProperty<LocalDate> dataProperty() { return data; }
    public StringProperty categoriaProperty() { return categoria; }

    // Getters e Setters simples
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }

    public String getDescricao() { return descricao.get(); }
    public void setDescricao(String descricao) { this.descricao.set(descricao); }

    public double getValor() { return valor.get(); }
    public void setValor(double valor) { this.valor.set(valor); }

    public LocalDate getData() { return data.get(); }
    public void setData(LocalDate data) { this.data.set(data); }

    public String getCategoria() { return categoria.get(); }
    public void setCategoria(String categoria) { this.categoria.set(categoria); }
}


