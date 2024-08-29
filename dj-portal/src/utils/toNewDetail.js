
import { encrypt } from "@/utils/encrypt"
export function toNewDetail(newSrc) {

/*  let sj = sm4Encrypt("测试数据")
  console.log("sm4Encrypt",sj)
  console.log("sm4Dncrypt",sm4Dncrypt(sj))*/
  newSrc=process.env.VUE_APP_YSZJ_ADDRESS+newSrc
  console.log(newSrc)
  this.$router.push({ path: "/newDetail" ,query:{newSrc:encrypt(newSrc)}});
}
