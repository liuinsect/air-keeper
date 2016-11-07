package com.air.airkeeper.ditributeworker.util;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: liukunyang
 * Date: 14-9-19
 * Time: 下午3:51
 * To change this template use File | Settings | File Templates.
 */
public class ListUtils {

    public static boolean isBlank(List list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(List list) {
        return !isBlank(list);
    }


    public static List randomList(List origin,int randomNum){
        List newList = new ArrayList();
        if( ListUtils.isBlank(origin) ){
            return newList;
        }

        Random random = new Random();

        Object item = null;
        int originNum = origin.size();
        for (int i = 0; i < originNum; i++) {

            if( newList.size() == randomNum ){
                break;
            }

            item = origin.get( random.nextInt(originNum) );
            if( !newList.contains(item) ){
                newList.add(item);
            }
        }

        return newList;
    }

    public static <T> boolean isEquals(final Collection<T> list1, final Collection<T> list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }

        return list1.containsAll(list2) && list2.containsAll(list1);
    }

    public static Collection subtract(Collection a, Collection b) {
        ArrayList list = new ArrayList(a);
        Iterator it = b.iterator();

        while(it.hasNext()) {
            list.remove(it.next());
        }

        return list;
    }


    public static Collection subList(List list,int maxSize) {
        if( (list == null) || list.size() <= maxSize ){
            return list;
        }
        return list.subList(0, maxSize);
    }

}
