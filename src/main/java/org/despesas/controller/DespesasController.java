package org.despesas.controller;

import org.despesas.dao.DespesaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.despesas.model.Despesa;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.text.NumberFormat;
import java.util.Locale;

public class DespesasController {

    // Componentes da UI (SceneBuilder)
    @FXML private TextField txtDescricao;
    @FXML private TextField txtValor;
    @FXML private DatePicker dpData;
    @FXML private ComboBox<String> cbCategoria;
    @FXML private TableView<Despesa> tvDespesas;
    @FXML private TableColumn<Despesa, Integer> colId;
    @FXML private TableColumn<Despesa, String> colDescricao;
    @FXML private TableColumn<Despesa, Double> colValor;
    @FXML private TableColumn<Despesa, LocalDate> colData;
    @FXML private TableColumn<Despesa, String> colCategoria;
    @FXML private Label lblTotal;

    private DespesaDAO despesaDAO;
    private ObservableList<Despesa> listaDespesas;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @FXML
    public void initialize() {
        despesaDAO = new DespesaDAO();
        listaDespesas = FXCollections.observableArrayList();

        // Configura as colunas da TableView
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // Formatação da coluna Valor
        colValor.setCellFactory(tc -> new TableCell<Despesa, Double>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                if (empty || valor == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(valor));
                }
            }
        });

        // Configura o ComboBox de Categorias
        List<String> categorias = Arrays.asList("Alimentação", "Transporte", "Lazer", "Moradia", "Saúde", "Outros");
        cbCategoria.setItems(FXCollections.observableArrayList(categorias));
        cbCategoria.getSelectionModel().selectFirst(); // Seleciona o primeiro item por padrão

        // Carrega os dados iniciais
        carregarDespesas();

        // Adiciona listener para seleção na tabela
        tvDespesas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> preencherFormulario(newValue));
    }

    private void carregarDespesas() {
        listaDespesas.clear();
        listaDespesas.addAll(despesaDAO.listarDespesas());
        tvDespesas.setItems(listaDespesas);
        atualizarTotal();
    }

    private void atualizarTotal() {
        double total = despesaDAO.calcularTotalDespesas();
        lblTotal.setText(currencyFormat.format(total));
    }

    private void preencherFormulario(Despesa despesa) {
        if (despesa != null) {
            txtDescricao.setText(despesa.getDescricao());
            txtValor.setText(String.valueOf(despesa.getValor()));
            dpData.setValue(despesa.getData());
            cbCategoria.getSelectionModel().select(despesa.getCategoria());
        } else {
            limparFormulario();
        }
    }

    private void limparFormulario() {
        txtDescricao.clear();
        txtValor.clear();
        dpData.setValue(LocalDate.now());
        cbCategoria.getSelectionModel().selectFirst();
        tvDespesas.getSelectionModel().clearSelection();
    }

    @FXML
    public void acaoBotaoAtualizar() {
        System.out.println("Atualizando lista..."); // Para você ver no console se funcionou
        carregarDespesas(); // Reutiliza o método que já existe!
    }

    @FXML
    private void handleAdicionarDespesa() {
        try {
            String descricao = txtDescricao.getText();
            // Permite vírgula como separador decimal
            double valor = Double.parseDouble(txtValor.getText().replace(",", "."));
            LocalDate data = dpData.getValue();
            String categoria = cbCategoria.getValue();

            if (descricao.isEmpty() || data == null || categoria == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Validação", "Todos os campos devem ser preenchidos.");
                return;
            }

            Despesa novaDespesa = new Despesa(descricao, valor, data, categoria);
            int id = despesaDAO.salvarDespesa(novaDespesa);

            if (id > 0) {
                listaDespesas.add(0, novaDespesa); // Adiciona no topo
                limparFormulario();
                atualizarTotal();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível salvar a despesa no banco de dados.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Formato", "O valor deve ser um número válido.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao adicionar a despesa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAtualizarDespesa() {
        Despesa despesaSelecionada = tvDespesas.getSelectionModel().getSelectedItem();
        if (despesaSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nenhuma Seleção", "Por favor, selecione uma despesa para atualizar.");
            return;
        }

        try {
            // Atualiza o objeto Despesa com os dados do formulário
            despesaSelecionada.setDescricao(txtDescricao.getText());
            despesaSelecionada.setValor(Double.parseDouble(txtValor.getText().replace(",", ".")));
            despesaSelecionada.setData(dpData.getValue());
            despesaSelecionada.setCategoria(cbCategoria.getValue());

            if (despesaDAO.atualizarDespesa(despesaSelecionada)) {
                tvDespesas.refresh(); // Atualiza a linha na TableView
                limparFormulario();
                atualizarTotal();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível atualizar a despesa no banco de dados.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Formato", "O valor deve ser um número válido.");
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao atualizar a despesa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExcluirDespesa() {
        Despesa despesaSelecionada = tvDespesas.getSelectionModel().getSelectedItem();
        if (despesaSelecionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nenhuma Seleção", "Por favor, selecione uma despesa para excluir.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Exclusão");
        alert.setHeaderText("Excluir Despesa ID: " + despesaSelecionada.getId());
        alert.setContentText("Tem certeza que deseja excluir a despesa '" + despesaSelecionada.getDescricao() + "'?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (despesaDAO.excluirDespesa(despesaSelecionada.getId())) {
                listaDespesas.remove(despesaSelecionada);
                limparFormulario();
                atualizarTotal();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível excluir a despesa do banco de dados.");
            }
        }
    }

    @FXML
    public void handleRecarregarTabela() {
        System.out.println("Recarregando tabela manualmente...");
        carregarDespesas(); // Reutiliza sua lógica pronta!
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

