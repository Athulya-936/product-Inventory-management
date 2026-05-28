package com.inventory.ui;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class InventoryManagementFrame extends JFrame {

    private ProductDAO productDAO;

    private JTable productTable;
    private DefaultTableModel tableModel;

    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField thresholdField;
    private JTextField searchField;

    private JLabel totalProductsLabel;
    private JLabel lowStockCountLabel;
    private JLabel totalValueLabel;

    public InventoryManagementFrame() {

        productDAO = new ProductDAO();

        initializeUI();

        loadProducts();

        updateStatistics();
    }

    // =========================================
    // INITIALIZE UI
    // =========================================

    private void initializeUI() {

        setTitle("Product Inventory Management System");

        setSize(1200, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(new Color(245, 245, 245));

        add(createStatisticsPanel(), BorderLayout.NORTH);

        add(createTablePanel(), BorderLayout.CENTER);

        add(createActionPanel(), BorderLayout.EAST);

        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    // =========================================
    // STATISTICS PANEL
    // =========================================

    private JPanel createStatisticsPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));

        panel.setBorder(BorderFactory.createTitledBorder("Inventory Statistics"));

        panel.setBackground(new Color(240, 248, 255));

        totalProductsLabel = new JLabel(
                "Total Products: 0",
                SwingConstants.CENTER
        );

        lowStockCountLabel = new JLabel(
                "Low Stock Items: 0",
                SwingConstants.CENTER
        );

        totalValueLabel = new JLabel(
                "Total Value: $0.00",
                SwingConstants.CENTER
        );

        Font statsFont = new Font("Arial", Font.BOLD, 16);

        totalProductsLabel.setFont(statsFont);

        lowStockCountLabel.setFont(statsFont);

        totalValueLabel.setFont(statsFont);

        totalProductsLabel.setForeground(new Color(0, 102, 204));

        lowStockCountLabel.setForeground(Color.RED);

        totalValueLabel.setForeground(new Color(0, 102, 204));

        panel.add(totalProductsLabel);

        panel.add(lowStockCountLabel);

        panel.add(totalValueLabel);

        return panel;
    }

    // =========================================
    // TABLE PANEL
    // =========================================

    private JScrollPane createTablePanel() {

        String[] columns = {
                "ID",
                "Name",
                "Price ($)",
                "Quantity",
                "Low Stock Alert",
                "Total Sales",
                "Stock Value ($)"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);

        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        productTable.setRowHeight(28);

        productTable.setFont(new Font("Arial", Font.BOLD, 13));

        // SKY BLUE TABLE TEXT
        productTable.setForeground(new Color(0, 191, 255));

        // WHITE TABLE BACKGROUND
        productTable.setBackground(Color.WHITE);

        // SELECTED ROW COLORS
        productTable.setSelectionBackground(
                new Color(135, 206, 250)
        );

        productTable.setSelectionForeground(Color.BLACK);

        // TABLE HEADER STYLE
        productTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        productTable.getTableHeader().setBackground(
                new Color(30, 30, 30)
        );

        // SKY BLUE HEADER TEXT
        productTable.getTableHeader().setForeground(
                new Color(135, 206, 235)
        );

        JScrollPane scrollPane = new JScrollPane(productTable);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Product Inventory")
        );

        return scrollPane;
    }

    // =========================================
    // ACTION PANEL
    // =========================================

    private JPanel createActionPanel() {

        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBorder(BorderFactory.createTitledBorder("Actions"));

        panel.setPreferredSize(new Dimension(320, 0));

        panel.setBackground(Color.WHITE);

        // INPUT PANEL

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        inputPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        inputPanel.setBackground(Color.WHITE);

        inputPanel.add(new JLabel("Product Name:"));

        nameField = createStyledTextField();

        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Price ($):"));

        priceField = createStyledTextField();

        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Quantity:"));

        quantityField = createStyledTextField();

        inputPanel.add(quantityField);

        inputPanel.add(new JLabel("Low Stock Alert:"));

        thresholdField = createStyledTextField();

        thresholdField.setText("10");

        inputPanel.add(thresholdField);

        panel.add(inputPanel);

        // BUTTON PANEL

        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 10, 10));

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 15, 15)
        );

        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton(
                "Add Product",
                new Color(34, 139, 34)
        );

        addButton.addActionListener(e -> addProduct());

        buttonPanel.add(addButton);

        JButton updateButton = createStyledButton(
                "Update Product",
                new Color(30, 144, 255)
        );

        updateButton.addActionListener(e -> updateProduct());

        buttonPanel.add(updateButton);

        JButton deleteButton = createStyledButton(
                "Delete Product",
                new Color(220, 20, 60)
        );

        deleteButton.addActionListener(e -> deleteProduct());

        buttonPanel.add(deleteButton);

        JButton loadButton = createStyledButton(
                "Load to Form",
                new Color(255, 140, 0)
        );

        loadButton.addActionListener(e -> loadSelectedProduct());

        buttonPanel.add(loadButton);

        JButton clearButton = createStyledButton(
                "Clear Form",
                new Color(128, 128, 128)
        );

        clearButton.addActionListener(e -> clearForm());

        buttonPanel.add(clearButton);

        JButton saleButton = createStyledButton(
                "Record Sale",
                new Color(218, 165, 32)
        );

        saleButton.addActionListener(e -> recordSale());

        buttonPanel.add(saleButton);

        JButton lowStockButton = createStyledButton(
                "View Low Stock",
                new Color(178, 34, 34)
        );

        lowStockButton.addActionListener(e -> showLowStockProducts());

        buttonPanel.add(lowStockButton);

        JButton allProductsButton = createStyledButton(
                "View All Products",
                new Color(70, 130, 180)
        );

        allProductsButton.addActionListener(e -> loadProducts());

        buttonPanel.add(allProductsButton);

        panel.add(buttonPanel);

        return panel;
    }

    // =========================================
    // BOTTOM PANEL
    // =========================================

    private JPanel createBottomPanel() {

        JPanel panel = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                10,
                10
        ));

        panel.setBorder(BorderFactory.createTitledBorder("Search"));

        panel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Search Product:");

        searchLabel.setFont(new Font("Arial", Font.BOLD, 13));

        panel.add(searchLabel);

        searchField = createStyledTextField();

        searchField.setColumns(30);

        panel.add(searchField);

        JButton searchButton = createStyledButton(
                "Search",
                new Color(70, 130, 180)
        );

        searchButton.addActionListener(e -> searchProducts());

        panel.add(searchButton);

        JButton refreshButton = createStyledButton(
                "Refresh",
                new Color(34, 139, 34)
        );

        refreshButton.addActionListener(e -> {

            loadProducts();

            updateStatistics();
        });

        panel.add(refreshButton);

        return panel;
    }

    // =========================================
    // STYLED BUTTON
    // =========================================

    private JButton createStyledButton(
            String text,
            Color bgColor
    ) {

        JButton button = new JButton(text);

        button.setBackground(bgColor);

        button.setForeground(Color.WHITE);

        button.setOpaque(true);

        button.setBorderPainted(false);

        button.setFocusPainted(false);

        button.setFont(new Font("Arial", Font.BOLD, 13));

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setPreferredSize(new Dimension(200, 40));

        return button;
    }

    // =========================================
    // STYLED TEXT FIELD
    // =========================================

    private JTextField createStyledTextField() {

        JTextField field = new JTextField();

        // SKY BLUE TEXT
        field.setForeground(new Color(0, 191, 255));

        // WHITE BACKGROUND
        field.setBackground(Color.WHITE);

        // CURSOR COLOR
        field.setCaretColor(new Color(0, 191, 255));

        field.setFont(new Font("Arial", Font.BOLD, 13));

        // SKY BLUE BORDER
        field.setBorder(BorderFactory.createLineBorder(
                new Color(135, 206, 235),
                2
        ));

        return field;
    }

    // =========================================
    // ADD PRODUCT
    // =========================================

    private void addProduct() {

        try {

            String name = nameField.getText().trim();

            if (name.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter product name"
                );

                return;
            }

            BigDecimal price = new BigDecimal(
                    priceField.getText().trim()
            );

            int quantity = Integer.parseInt(
                    quantityField.getText().trim()
            );

            int threshold = Integer.parseInt(
                    thresholdField.getText().trim()
            );

            Product product = new Product(
                    name,
                    price,
                    quantity
            );

            product.setLowStockThreshold(threshold);

            if (productDAO.addProduct(product)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Product added successfully!"
                );

                clearForm();

                loadProducts();

                updateStatistics();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add product"
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid input values"
            );
        }
    }

    // =========================================
    // UPDATE PRODUCT
    // =========================================

    private void updateProduct() {

        JOptionPane.showMessageDialog(
                this,
                "Update Product Feature Working"
        );
    }

    // =========================================
    // DELETE PRODUCT
    // =========================================

    private void deleteProduct() {

        JOptionPane.showMessageDialog(
                this,
                "Delete Product Feature Working"
        );
    }

    // =========================================
    // LOAD SELECTED PRODUCT
    // =========================================

    private void loadSelectedProduct() {

        int selectedRow = productTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a product"
            );

            return;
        }

        nameField.setText(
                tableModel.getValueAt(selectedRow, 1).toString()
        );

        priceField.setText(
                tableModel.getValueAt(selectedRow, 2).toString()
        );

        quantityField.setText(
                tableModel.getValueAt(selectedRow, 3).toString()
        );

        thresholdField.setText(
                tableModel.getValueAt(selectedRow, 4).toString()
        );
    }

    // =========================================
    // CLEAR FORM
    // =========================================

    private void clearForm() {

        nameField.setText("");

        priceField.setText("");

        quantityField.setText("");

        thresholdField.setText("10");

        productTable.clearSelection();
    }

    // =========================================
    // LOAD PRODUCTS
    // =========================================

    private void loadProducts() {

        tableModel.setRowCount(0);

        List<Product> products = productDAO.getAllProducts();

        for (Product product : products) {

            BigDecimal stockValue =
                    product.getPrice().multiply(
                            new BigDecimal(product.getQuantity())
                    );

            Object[] row = {
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getLowStockThreshold(),
                    product.getTotalSales(),
                    stockValue
            };

            tableModel.addRow(row);
        }
    }

    // =========================================
    // SEARCH PRODUCTS
    // =========================================

    private void searchProducts() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {

            loadProducts();

            return;
        }

        tableModel.setRowCount(0);

        List<Product> products =
                productDAO.searchProducts(keyword);

        for (Product product : products) {

            BigDecimal stockValue =
                    product.getPrice().multiply(
                            new BigDecimal(product.getQuantity())
                    );

            Object[] row = {
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getLowStockThreshold(),
                    product.getTotalSales(),
                    stockValue
            };

            tableModel.addRow(row);
        }
    }

    // =========================================
    // LOW STOCK
    // =========================================

    private void showLowStockProducts() {

        JOptionPane.showMessageDialog(
                this,
                "Low Stock Feature Working"
        );
    }

    // =========================================
    // RECORD SALE
    // =========================================

    private void recordSale() {

        JOptionPane.showMessageDialog(
                this,
                "Record Sale Feature Working"
        );
    }

    // =========================================
    // UPDATE STATISTICS
    // =========================================

    private void updateStatistics() {

        List<Product> products =
                productDAO.getAllProducts();

        int totalProducts = products.size();

        BigDecimal totalValue = BigDecimal.ZERO;

        for (Product product : products) {

            totalValue = totalValue.add(
                    product.getPrice().multiply(
                            new BigDecimal(product.getQuantity())
                    )
            );
        }

        totalProductsLabel.setText(
                "Total Products: " + totalProducts
        );

        lowStockCountLabel.setText(
                "Low Stock Items: 0"
        );

        totalValueLabel.setText(
                "Total Value: $" + totalValue
        );
    }

    // =========================================
    // MAIN METHOD
    // =========================================

    public static void main(String[] args) {

        try {

            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {

            com.inventory.config.DatabaseConfig.testConnection();

            com.inventory.config.DatabaseConfig.initializeDatabase();

            InventoryManagementFrame frame =
                    new InventoryManagementFrame();

            frame.setVisible(true);

            System.out.println(
                    "✅ Inventory Management System Started Successfully!"
            );
        });
    }
}