import CryptoJS from 'crypto-js'

// 自定义密钥
const KEY = CryptoJS.enc.Utf8.parse('0895FE62495911B2')

// AES加密
export function encrypt(word) {
  const encrypted = CryptoJS.AES.encrypt(word, KEY, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  })
  return encrypted.toString()
}

// AES解密
export function decrypt(word) {
  const decrypted = CryptoJS.AES.decrypt(word, KEY, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  })
  return decrypted.toString(CryptoJS.enc.Utf8)
}

/*// AES加密
export function encrypt(word) {
  const encrypted = CryptoJS.AES.encrypt(word, KEY, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  }).toString()
  //对加密数据进行base64处理, 原理：就是先将字符串转换为utf8字符数组，再转换为base64数据
  return CryptoJS.enc.Base64.stringify(CryptoJS.enc.Utf8.parse(encrypted));
}

// AES解密
export function decrypt(word) {
  let decData = CryptoJS.enc.Base64.parse(word).toString(CryptoJS.enc.Utf8);
  const decrypted = CryptoJS.AES.decrypt(decData, KEY, {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  })
  //解密数据
  return decrypted.toString(CryptoJS.enc.Utf8);
}*/

//国密sm4加密

const sm4 = require('sm-crypto').sm4
//秘钥
const SM4KEY = "EA1E5B99838B9F5EEB059732DB0C3071"
// 加密
export function sm4Encrypt(data) {
  // sm4加密数据
  return sm4.encrypt(data, SM4KEY);
}

// 解密
export function sm4Dncrypt(data) {
  // sm4解密数据
  return sm4.decrypt(data, SM4KEY)
}
