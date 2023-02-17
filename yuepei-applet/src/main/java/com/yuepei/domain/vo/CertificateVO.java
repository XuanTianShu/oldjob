package com.yuepei.domain.vo;

import lombok.Data;

/**
 * 　　　　 ┏┓       ┏┓+ +
 * 　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　┃　　　　　　 ┃
 * 　　　　┃　　　━　　　┃ ++ + + +
 * 　　　 █████━█████  ┃+
 * 　　　　┃　　　　　　 ┃ +
 * 　　　　┃　　　┻　　　┃
 * 　　　　┃　　　　　　 ┃ + +
 * 　　　　┗━━┓　　　 ┏━┛
 * 　　　　　　┃　　  ┃
 * 　　　　　　┃　　  ┃ + + + +
 * 　　　　　　┃　　　┃　Code is far away from bug with the animal protection
 * 　　　　　　┃　　　┃ + 　　　　         神兽保佑,代码永无bug
 * 　　　　　　┃　　　┃
 * 　　　　　　┃　　　┃　　+
 * 　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　┃ 　　　　　┣-┓
 * 　　　　　　┃ 　　　　　┏-┛
 * 　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 * *********************************************************
 *
 * @author ：AK
 * @create ：2023/1/5 16:59
 **/
public class CertificateVO {


    /**
     * 平台证书序列号
     */
    private String serial_no;

    /**
     * 平台证书有效时间
     */
    private String effective_time;

    /**
     * 平台证书过期时间
     */
    private String expire_time;

    /**
     * 加密证书
     */
    private EncryptCertificate encrypt_certificate;

    /**
     * 加密证书
     */
    public class EncryptCertificate {

        /**
         * 算法
         */
        private String algorithm;

        /**
         * 随机字符串
         */
        private String nonce;

        /**
         * 相关数据
         */
        private String associated_data;

        /**
         * 密文
         */
        private String ciphertext;

        public String getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getAssociated_data() {
            return associated_data;
        }

        public void setAssociated_data(String associated_data) {
            this.associated_data = associated_data;
        }

        public String getCiphertext() {
            return ciphertext;
        }

        public void setCiphertext(String ciphertext) {
            this.ciphertext = ciphertext;
        }
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getEffective_time() {
        return effective_time;
    }

    public void setEffective_time(String effective_time) {
        this.effective_time = effective_time;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public EncryptCertificate getEncrypt_certificate() {
        return encrypt_certificate;
    }

    public void setEncrypt_certificate(EncryptCertificate encrypt_certificate) {
        this.encrypt_certificate = encrypt_certificate;
    }
}
