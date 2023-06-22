package main.java.utilities;
import java.util.List;
import java.util.Arrays;

import utilities.*;
//public class MathUtil extends BaseClassAPI{
//    public static String warehouseId = null;
//
//    public static void warehouseDetails(String[] args) {
//        List<Integer> list = Arrays.asList(Integer.valueOf(warehouseId));
//
//        int index = 2;
//        if (index >= 0 && index < list.size()) {
//            int wareHouseId = list.get(index);
//            System.out.println("Value at index " + index + ": " + wareHouseId);
//        } else {
//            System.out.println("Invalid index.");
//        }
//    }
//}

public class MathUtil{
    public static int function(List<Integer> arr){
        int index = 2, warehouseId = 0;
        if (index >= 0 && index < arr.size()) {
            warehouseId = arr.get(index);
//            System.out.println("Value at index " + index + ": " + warehouseId);
        } else {
            System.out.println("Invalid index.");
        }
        return warehouseId;
    }
}
