/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.dasi.collectif.view;

import fr.insalyon.dasi.collectif.dao.JpaUtil;

/**
 *
 * @author tbourvon
 */
public class Main {
    public static void main(String[] args) {
        JpaUtil.init();
    
        
        
        JpaUtil.destroy();
    }
}
