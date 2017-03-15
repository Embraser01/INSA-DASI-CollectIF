/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.collectif.business.service;


/**
 * @author tbourvon
 */
public class TechnicalService {

    public boolean sendMail(String sendTo, String subject, String content) {
        System.out.println("---------------------------------------------------");

        System.out.println("Expéditeur : collectif@collectif.org");
        System.out.println("Pour : " + sendTo);
        System.out.println("Sujet : " + subject);
        System.out.println("Corps :\n");

        System.out.println(content);

        System.out.println("\n---------------------------------------------------");
        return true;
    }
}
