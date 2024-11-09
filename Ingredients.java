package Ingredients;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Ingredients {
    private JTextField categoryText;
    private JButton saveButton;
    private JTable table1;
    private JButton deleteButton;
    private JTextField nameText;
    private JTextField qtyText;
    private JPanel mainPanel;
    private JButton updateButton;
    private JButton searchButton;
    private JTextField idText;

    Connection connection;
    PreparedStatement preparedStatement;

    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame("Ingredients");
        frame.setContentPane(new Ingredients().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void connect() throws SQLException {

        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ingredient", "root", "02012002");
        System.out.println("Successfully Connected");

    }

    void loadTable() throws SQLException {
        preparedStatement = connection.prepareStatement("Select * From Ingredients");
        ResultSet result = preparedStatement.executeQuery();
        table1.setModel(DbUtils.resultSetToTableModel(result));
    }

    public Ingredients() throws SQLException {
        connect();
        loadTable();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String category, name;
                int quantity;
                category = categoryText.getText();
                name = nameText.getText();
                quantity = Integer.parseInt(qtyText.getText());
                try {
                    preparedStatement = connection.prepareStatement("Insert Into Ingredients(Category,Name, Quantity) Values (?,?,?)");
                    preparedStatement.setString(1, category);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, String.valueOf(quantity));
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "New Ingredient Saved!");
                    //clear the text field
                    nameText.setText("");
                    categoryText.setText("");
                    qtyText.setText("");
                    categoryText.requestFocus();
                    loadTable();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }

        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idText.getText();
                try {
                    preparedStatement = connection.prepareStatement("Select Category, Name, Quantity from Ingredients where id=?");
                    preparedStatement.setString(1,id);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if(resultSet.next())
                    {
                        // get information from table
                        String category = resultSet.getString(1);
                        String name = resultSet.getString(2);
                        String qty = resultSet.getString(3);

                        // display them to gui
                        categoryText.setText(category);
                        nameText.setText(name);
                        qtyText.setText(qty);
                    }
                    else {
                        categoryText.setText("");
                        nameText.setText("");
                        qtyText.setText("");
                        JOptionPane.showMessageDialog(null,"Item does not exist");
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id, category, name, quantity;
                id = idText.getText();
                category = categoryText.getText();
                name = nameText.getText();
                quantity = qtyText.getText();
                try {
                    preparedStatement = connection.prepareStatement("Update Ingredients set Category =? , Name = ?, Quantity = ? where id = ?");
                    preparedStatement.setString(4,id);
                    preparedStatement.setString(1,category);
                    preparedStatement.setString(2,name);
                    preparedStatement.setString(3,quantity);
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Record Updated");
                    loadTable();
                    idText.setText("");
                    categoryText.setText("");
                    nameText.setText("");
                    qtyText.setText("");

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id;
                id = idText.getText();
                try{
                    preparedStatement = connection.prepareStatement("Delete from Ingredients where id=?");
                    preparedStatement.setString(1,id);
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Item deleted");
                    loadTable();
                    idText.setText("");
                    categoryText.setText("");
                    nameText.setText("");
                    qtyText.setText("");

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
