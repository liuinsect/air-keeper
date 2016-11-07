package com.air.airkeeper.ditributeworker.util;

import io.atomix.catalyst.transport.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liukunyang on 16-6-10.
 *
 * @author liukunyang
 * @date 16-6-10
 */
public class AddressUtil {

    private static Logger logger = LoggerFactory.getLogger(AddressUtil.class);

    public static final String UNDERLINE = "_";

    public static final String COLON = ":";

    public static List<Address> resolveAddress(String configLine){
        List<Address> inetSocketAddressList = new ArrayList<Address>();
        if(StringUtils.isNotBlank(configLine)){
            Address address = null;
            String[] lines = configLine.split(UNDERLINE);
            for( String line:lines ){
                address = resolved(line);
                if( address != null ){
                    inetSocketAddressList.add(address);
                }
            }

        }
        return inetSocketAddressList;
    }

    private static Address resolved(String config){
        try {
            String[] tmp = config.split(COLON);
            String ip = tmp[0];
            String port = tmp[1];

            Address inetSocketAddress = new Address(ip,Integer.valueOf(port));

            return inetSocketAddress;
        }catch (Exception e){
            logger.error( " resolved config failed,config:"+config ,e);
        }
        return  null;
    }

}
