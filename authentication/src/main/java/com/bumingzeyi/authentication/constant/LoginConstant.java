package com.bumingzeyi.authentication.constant;

/**
 * @author :BuMing
 * @date : 2022-03-27 21:22
 */
public interface LoginConstant {

    /**
     * 登录模板
     */
    public static String LOGIN_TEMPLATE = "<Envelope xmlns='http://schemas.xmlsoap.org/soap/envelope/'"
            + "       xmlns:wsse='http://schemas.xmlsoap.org/ws/2003/06/secext'"
            + "       xmlns:wsp='http://schemas.xmlsoap.org/ws/2002/12/policy'"
            + "       xmlns:wsa='http://schemas.xmlsoap.org/ws/2004/03/addressing'"
            + "       xmlns:wst='http://schemas.xmlsoap.org/ws/2004/04/trust'"
            + "       xmlns:ps='http://schemas.microsoft.com/Passport/SoapServices/PPCRL'>"
            + "       <Header>"
            + "           <wsse:Security>"
            + "               <wsse:UsernameToken Id='user'>"
            + "                   <wsse:Username>%s</wsse:Username>"
            + "                   <wsse:Password>%s</wsse:Password>"
            + "               </wsse:UsernameToken>"
            + "           </wsse:Security>"
            + "       </Header>"
            + "       <Body>"
            + "           <ps:RequestMultipleSecurityTokens Id='RSTS'>"
            + "               <wst:RequestSecurityToken Id='RST0'>"
            + "                   <wst:RequestType>http://schemas.xmlsoap.org/ws/2004/04/security/trust/Issue</wst:RequestType>"
            + "                   <wsp:AppliesTo>"
            + "                       <wsa:EndpointReference>"
            + "                           <wsa:Address>wl.skype.com</wsa:Address>"
            + "                       </wsa:EndpointReference>"
            + "                   </wsp:AppliesTo>"
            + "                   <wsse:PolicyReference URI='MBI_SSL'></wsse:PolicyReference>"
            + "               </wst:RequestSecurityToken>"
            + "           </ps:RequestMultipleSecurityTokens>"
            + "       </Body>"
            + "    </Envelope>";

    //账户密码登录地址
    String LOGIN_URL = "https://login.live.com/RST.srf";

    /* 解析responseBody拿到token */
    String TOKEN_TAG_START = "<wsse:BinarySecurityToken Id=\"Compact0\">";

    String TOKEN_TAG_END = "</wsse:BinarySecurityToken>";

}
