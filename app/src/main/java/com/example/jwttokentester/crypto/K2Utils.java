package com.example.jwttokentester.crypto;

/**
 * Created by danielm on 07/02/2018.
 */

public class K2Utils {
    public final static String ENDESA_KP = "FEB0FEB0FEB0FEB0FEB0FEB0FEB0FEB0";
    public final static String ENDESA_K0 = "00000000000000000000000000000000";
    public final static String ENEL_KP = "FEB0FEB0FEB0FEB0FEB0FEB0";
    public final static String ENEL_K0 = "000000000000000000000000";

    public static String verifyK2(String matricola) {
        String K2Clear = null;
//        try {
        String k2Crypted = "";//TODO (String) LegoApplication.getRealmInstance().getField("PERFORMER", "K2_PERFORMER_UNIT", false);
//            String matUserK2 = (String) LegoApplication.getRealmInstance().getField("PERFORMER", "PERFORMER_IDENTIFIER", false);

//            K2Clear = retrieveK2(k2Crypted, matricola);

//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        return K2Clear;
    }

//    public static String retrieveK2(String k2Crypted, String matricola) {
//        String result;
//        String kco = getKCO(matricola);
//        if (BlowFish.getErrorList().contains(kco)) {
//            return kco;
//        }
//        result = BlowFish.decryptBlowfish(k2Crypted, kco);
//        return result;
//    }

//    public static boolean checkDecryptK2(String k2Crypted, String matricola){
//        String result = retrieveK2(k2Crypted, matricola);
////        LegoLogHelper.info("PROVA"+result);
//        return !(result.contains("ERR") || result == null);
//    }

    public static String retrieveK2(String k2Crypted, String matricola, String pop) {
        String result;
        String kco = BlowFish.getKCO(matricola, getPasswordADLK2(matricola), pop);
        if (BlowFish.getErrorList().contains(kco)) {
            return kco;
        }
        result = BlowFish.decryptBlowfish(k2Crypted, kco);
        return result;
    }

//    public static String getKCO(String matricola){
//        return BlowFish.getKCO(matricola.toUpperCase(), getPasswordADLK2(matricola), getPasswordTabEsecutoreADLK2());
//    }

    public static String getPasswordADLK2(String matricola) {
        return matricola.substring(2).toLowerCase() + "$Z89" + matricola.substring(matricola.length() - 1);
    }

//    public static String getPasswordTabEsecutoreADLK2(){
//        return DatabaseDataSource.getDatiExtraDbInstance().inputModelDao().getValue(K2_BUNDLE, FIELD_POP).getValue();
//    }

    public static String decryptK2() {
        try {
            return "";

        } catch (Exception e) {
//            LegoLogHelper.error(e.getMessage(),e);
            return null;
        }
    }

}
