module org.despesas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Necessário para o PostgreSQL
    requires org.postgresql.jdbc;
    requires org.jetbrains.annotations; // Necessário para o Driver

    // Permite que o JavaFX "veja" a pasta principal
    opens org.despesas to javafx.fxml;

    // --- A LINHA QUE FALTA ESTÁ AQUI EMBAIXO ---
    // Você precisa abrir especificamente o pacote onde está o Controller
    opens org.despesas.controller to javafx.fxml;

    // Se você tiver classes na pasta model que usa na Tabela (TableView),
    // precisa abrir ela também para a tabela conseguir ler os dados:
    opens org.despesas.model to javafx.base;

    exports org.despesas;
}