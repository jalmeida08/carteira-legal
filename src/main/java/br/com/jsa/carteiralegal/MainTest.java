package br.com.jsa.carteiralegal;

import java.time.LocalDate;

public class MainTest {

    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        System.out.println(LocalDate.of(localDate.getYear(), localDate.getMonthValue(), LocalDate.MIN.getDayOfMonth()));
        System.out.println(LocalDate.of(localDate.getYear(), localDate.getMonthValue(), LocalDate.MAX.getDayOfMonth()));
    }
}
