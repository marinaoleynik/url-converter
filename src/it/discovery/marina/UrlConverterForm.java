package it.discovery.marina;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UrlConverterForm extends JFrame {
    private JTextField textForLongUrl;
    private JButton convertLongUrl;
    private JTextField textForShortUrl;
    private JButton convertShortUrl;
    private JTextField shortUrlLabel;
    private JLabel longUrlLabel;
    private JLabel label1;
    private JLabel label2;

    public UrlConverterForm()
    {
        super("UrlConverterForm");
        setLayout(new FlowLayout());
        textForLongUrl = new JTextField(40);
        convertLongUrl = new JButton("Convert");
        textForShortUrl = new JTextField(25);
        convertShortUrl = new JButton("GetFullURL");
        shortUrlLabel = new JTextField(20);
        longUrlLabel = new JLabel("");
        label1 = new JLabel("Put long URL:");
        label2= new JLabel("Put short URL:");
        add(label1);
        add(textForLongUrl);
        add(convertLongUrl);

        add(shortUrlLabel);

        add(label2);
        add(textForShortUrl);
        add(convertShortUrl);

        add(longUrlLabel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setVisible(true);

        convertLongUrl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String longUrl = textForLongUrl.getText();
                UrlConverter urlConverter = new UrlConverter();
                shortUrlLabel.setText(urlConverter.convertUrl(longUrl));
            }
        });

        convertShortUrl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String shortUrl = textForShortUrl.getText();
                UrlConverter urlConverter = new UrlConverter();
                longUrlLabel.setText(urlConverter.getFullUrl(shortUrl));
            }
        });
    }

    public static void main(String[] args) {
         UrlConverterForm form = new UrlConverterForm();
    }


}
