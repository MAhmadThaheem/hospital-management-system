package com.hms.main;

import com.hms.model.*;
import com.hms.service.*;
import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.*;

public class Main extends JFrame {
    private final AppointmentService appointmentService = new AppointmentService();
    private final InventoryService inventoryService = new InventoryService();
    private final InsuranceService insuranceService = new InsuranceService();
    private final BillingService billingService = new BillingService();
    private final PaymentService paymentService = new PaymentService();

    private final java.util.List<Patient> patients = new ArrayList<>();
    private final java.util.List<Doctor> doctors = new ArrayList<>();

    private final JPanel contentPanel = new JPanel(new CardLayout());

    public Main() {
        setTitle("🏥 Hospital Management System");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Sidebar
        JPanel sidebar = new JPanel(new GridLayout(10, 1, 5, 5));
        sidebar.setBackground(new Color(30, 60, 114));
        sidebar.setPreferredSize(new Dimension(200, 650));

        String[] buttons = {
            "Add Patient", "Add Doctor", "Add Medicine",
            "Book Appointment", "Cancel Appointment",
            "Generate Bill", "Make Payment", "View Collected", "Exit"
        };

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(new Color(58, 123, 213));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            btn.addActionListener(e -> showPanel(text));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(80, 150, 250));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(58, 123, 213));
                }
            });
            sidebar.add(btn);
        }

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Register panels
        contentPanel.add(addPatientPanel(), "Add Patient");
        contentPanel.add(addDoctorPanel(), "Add Doctor");
        contentPanel.add(addMedicinePanel(), "Add Medicine");
        contentPanel.add(bookAppointmentPanel(), "Book Appointment");
        contentPanel.add(cancelAppointmentPanel(), "Cancel Appointment");
        contentPanel.add(generateBillPanel(), "Generate Bill");
        contentPanel.add(makePaymentPanel(), "Make Payment");
        contentPanel.add(viewCollectedPanel(), "View Collected");

        setVisible(true);
    }

    private void showPanel(String name) {
        if (name.equals("Exit")) {
            dispose();
            return;
        }
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, name);
    }

    // Helper method for nice form panels
    private JPanel createFormPanel(String title, JComponent... comps) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setBackground(new Color(245, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        for (JComponent c : comps) {
            panel.add(c, gbc);
            gbc.gridy++;
        }
        return panel;
    }

    // ---------- Panels ----------
    private JPanel addPatientPanel() {
        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField age = new JTextField();
        JTextField plan = new JTextField();
        JButton add = new JButton("Add Patient");

        JPanel p = createFormPanel("Add Patient",
            new JLabel("Patient ID:"), id,
            new JLabel("Name:"), name,
            new JLabel("Age:"), age,
            new JLabel("Insurance Plan (gold/silver/none):"), plan,
            add
        );

        add.addActionListener(e -> {
            try {
                int pid = Integer.parseInt(id.getText());
                patients.add(new Patient(pid, name.getText(), Integer.parseInt(age.getText()), plan.getText()));
                JOptionPane.showMessageDialog(this, "✅ Patient added successfully!");
                id.setText(""); name.setText(""); age.setText(""); plan.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Invalid input!");
            }
        });

        return p;
    }

    private JPanel addDoctorPanel() {
        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField spec = new JTextField();
        JTextField fee = new JTextField();
        JButton add = new JButton("Add Doctor");

        JPanel p = createFormPanel("Add Doctor",
            new JLabel("Doctor ID:"), id,
            new JLabel("Name:"), name,
            new JLabel("Specialization:"), spec,
            new JLabel("Fee per hour:"), fee,
            add
        );

        add.addActionListener(e -> {
            try {
                int did = Integer.parseInt(id.getText());
                double df = Double.parseDouble(fee.getText());
                doctors.add(new Doctor(did, name.getText(), spec.getText(), df));
                JOptionPane.showMessageDialog(this, "✅ Doctor added!");
                id.setText(""); name.setText(""); spec.setText(""); fee.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Invalid input!");
            }
        });

        return p;
    }

    private JPanel addMedicinePanel() {
        JTextField name = new JTextField();
        JTextField price = new JTextField();
        JTextField qty = new JTextField();
        JTextField exp = new JTextField();
        JButton add = new JButton("Add Medicine");

        JPanel p = createFormPanel("Add Medicine",
            new JLabel("Medicine Name:"), name,
            new JLabel("Unit Price:"), price,
            new JLabel("Quantity:"), qty,
            new JLabel("Expiry Date (YYYY-MM-DD):"), exp,
            add
        );

        add.addActionListener(e -> {
            try {
                LocalDate date = exp.getText().isEmpty() ? null : LocalDate.parse(exp.getText());
                Medicine m = new Medicine(name.getText(), Double.parseDouble(price.getText()),
                        Integer.parseInt(qty.getText()), date);
                boolean added = inventoryService.addMedicine(m);
                JOptionPane.showMessageDialog(this, added ? "✅ Medicine added!" : "⚠️ Already exists!");
                name.setText(""); price.setText(""); qty.setText(""); exp.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Invalid input!");
            }
        });

        return p;
    }

    private JPanel bookAppointmentPanel() {
        JComboBox<Patient> patientBox = new JComboBox<>();
        JComboBox<Doctor> doctorBox = new JComboBox<>();
        JTextField start = new JTextField();
        JTextField end = new JTextField();
        JButton book = new JButton("Book Appointment");

        JPanel p = createFormPanel("Book Appointment",
            new JLabel("Select Patient:"), patientBox,
            new JLabel("Select Doctor:"), doctorBox,
            new JLabel("Start (yyyy-MM-ddTHH:mm):"), start,
            new JLabel("End (yyyy-MM-ddTHH:mm):"), end,
            book
        );

        book.addActionListener(e -> {
            try {
                Patient pat = (Patient) patientBox.getSelectedItem();
                Doctor doc = (Doctor) doctorBox.getSelectedItem();
                LocalDateTime s = LocalDateTime.parse(start.getText());
                LocalDateTime en = LocalDateTime.parse(end.getText());
                boolean ok = appointmentService.book(new Appointment(pat, doc, s, en));
                JOptionPane.showMessageDialog(this, ok ? "✅ Appointment booked!" : "❌ Clash occurred!");
                start.setText(""); end.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Invalid input!");
            }
        });

        p.addHierarchyListener(e -> {
            if (p.isShowing()) {
                patientBox.removeAllItems();
                doctorBox.removeAllItems();
                for (Patient pa : patients) patientBox.addItem(pa);
                for (Doctor d : doctors) doctorBox.addItem(d);
            }
        });

        return p;
    }

    private JPanel cancelAppointmentPanel() {
        JTextField id = new JTextField();
        JButton cancel = new JButton("Cancel");

        JPanel p = createFormPanel("Cancel Appointment",
            new JLabel("Appointment ID:"), id, cancel
        );

        cancel.addActionListener(e -> {
            try {
                int apid = Integer.parseInt(id.getText());
                boolean ok = appointmentService.cancel(apid);
                JOptionPane.showMessageDialog(this, ok ? "✅ Appointment cancelled!" : "❌ Not found!");
                id.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Invalid input!");
            }
        });
        return p;
    }

    private JPanel generateBillPanel() {
    JPanel p = new JPanel(new BorderLayout(10, 10));

    // Top Section: Patient + Medicine Selection
    JPanel top = new JPanel(new GridLayout(3, 2, 5, 5));
    JComboBox<Patient> patientBox = new JComboBox<>();
    JComboBox<Medicine> medicineBox = new JComboBox<>();
    JTextField qtyField = new JTextField();
    JButton addMedicineBtn = new JButton("Add Medicine");
    JButton generateBtn = new JButton("Generate Bill");

    top.add(new JLabel("Select Patient:"));
    top.add(patientBox);
    top.add(new JLabel("Select Medicine:"));
    top.add(medicineBox);
    top.add(new JLabel("Quantity:"));
    top.add(qtyField);

    JPanel mid = new JPanel(new BorderLayout());
    DefaultListModel<String> medListModel = new DefaultListModel<>();
    JList<String> medList = new JList<>(medListModel);
    mid.add(new JScrollPane(medList), BorderLayout.CENTER);

    JPanel bottom = new JPanel(new BorderLayout());
    JTextArea result = new JTextArea(10, 40);
    result.setEditable(false);
    bottom.add(new JScrollPane(result), BorderLayout.CENTER);
    bottom.add(generateBtn, BorderLayout.SOUTH);

    p.add(top, BorderLayout.NORTH);
    p.add(mid, BorderLayout.CENTER);
    p.add(bottom, BorderLayout.SOUTH);

    // Local map for selected medicines
    Map<Medicine, Integer> selectedMeds = new HashMap<>();

    // Refresh patient and medicine lists when shown
    p.addHierarchyListener(e -> {
        if (p.isShowing()) {
            patientBox.removeAllItems();
            medicineBox.removeAllItems();
            for (Patient pa : patients) patientBox.addItem(pa);
            for (Medicine m : inventoryService.getAll()) medicineBox.addItem(m);
        }
    });

    // Add medicine to bill
    addMedicineBtn.addActionListener(e -> {
        try {
            Medicine m = (Medicine) medicineBox.getSelectedItem();
            int q = Integer.parseInt(qtyField.getText());
            if (m == null || q <= 0) throw new Exception();

            if (q > m.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Not enough stock!");
                return;
            }

            selectedMeds.put(m, selectedMeds.getOrDefault(m, 0) + q);
            medListModel.addElement(m.getName() + " x " + q);
            qtyField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    });

    // Generate final bill
    generateBtn.addActionListener(e -> {
        Patient pat = (Patient) patientBox.getSelectedItem();
        if (pat == null || selectedMeds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select patient and medicines!");
            return;
        }

        // Calculate total for selected medicines
        double medTotal = 0;
        for (Map.Entry<Medicine, Integer> entry : selectedMeds.entrySet()) {
            Medicine m = entry.getKey();
            int qty = entry.getValue();
            medTotal += m.getPrice() * qty;
            inventoryService.deductStock(m.getName(), qty);
        }

        // Add appointment charges for selected patient
        double appointmentTotal = appointmentService.getAll().stream()
                .filter(a -> a.getPatient().equals(pat))
                .mapToDouble(a -> a.getDoctor().getFee())
                .sum();

        double subtotal = medTotal + appointmentTotal;
        double rate = insuranceService.getCoverageRate(pat.getInsuranceType());
        double afterIns = billingService.applyInsurance(subtotal, rate);
        double finalAmt = billingService.applyTax(afterIns, 5.0);

        result.setText(
                "Patient: " + pat.getName() + "\n" +
                "Medicine Total: " + medTotal + "\n" +
                "Appointment Total: " + appointmentTotal + "\n" +
                "Subtotal: " + subtotal + "\n" +
                "After Insurance: " + afterIns + "\n" +
                "Final (after 5% tax): " + finalAmt
        );

        selectedMeds.clear();
        medListModel.clear();
        JOptionPane.showMessageDialog(this, "Bill generated successfully!");
    });

    // Add the medicine button below medicine selection row
    JPanel medActionPanel = new JPanel();
    medActionPanel.add(addMedicineBtn);
    p.add(medActionPanel, BorderLayout.EAST);

    return p;
}


    private final Map<Patient, Double> pendingBills = new HashMap<>();

private JPanel makePaymentPanel() {
    JPanel p = new JPanel(new GridLayout(4, 2, 5, 5));
    JComboBox<Patient> patientBox = new JComboBox<>();
    JTextField billField = new JTextField();
    billField.setEditable(false);
    JButton fetch = new JButton("Show Bill");
    JButton pay = new JButton("Pay Now");

    p.add(new JLabel("Select Patient:"));
    p.add(patientBox);
    p.add(new JLabel("Bill Amount:"));
    p.add(billField);
    p.add(fetch);
    p.add(pay);

    // Load patients dynamically
    p.addHierarchyListener(e -> {
        if (p.isShowing()) {
            patientBox.removeAllItems();
            for (Patient pa : patients) patientBox.addItem(pa);
        }
    });

    // Fetch bill button
    fetch.addActionListener(e -> {
        Patient pat = (Patient) patientBox.getSelectedItem();
        if (pat == null) {
            JOptionPane.showMessageDialog(this, "Select a patient first!");
            return;
        }

        // Recalculate bill
        double rate = insuranceService.getCoverageRate(pat.getInsuranceType());
        double subtotal = billingService.calculateSubTotal(inventoryService.getAll(), appointmentService.getAll());
        double afterIns = billingService.applyInsurance(subtotal, rate);
        double finalAmt = billingService.applyTax(afterIns, 5.0);

        // Check if already paid
        if (pendingBills.getOrDefault(pat, finalAmt) == 0.0) {
            billField.setText("No bill pending");
        } else {
            pendingBills.put(pat, finalAmt);
            billField.setText(String.valueOf(finalAmt));
        }
    });

    // Pay Now button
    pay.addActionListener(e -> {
        Patient pat = (Patient) patientBox.getSelectedItem();
        if (pat == null) {
            JOptionPane.showMessageDialog(this, "Select a patient!");
            return;
        }
        Double bill = pendingBills.get(pat);
        if (bill == null || bill == 0.0) {
            JOptionPane.showMessageDialog(this, "No bill to pay!");
            return;
        }

        paymentService.acceptPayment(bill);
        pendingBills.put(pat, 0.0); // mark as paid
        billField.setText("Paid ✅");
        JOptionPane.showMessageDialog(this, "Payment successful for " + pat.getName());
    });

    return p;
}


    private JPanel viewCollectedPanel() {
        JTextArea area = new JTextArea();
        JButton refresh = new JButton("Refresh");

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("View Total Collection"));
        p.add(refresh, BorderLayout.NORTH);
        p.add(new JScrollPane(area), BorderLayout.CENTER);

        refresh.addActionListener(e -> {
            area.setText("Total collected: " + paymentService.getCollectedAmount());
        });

        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
