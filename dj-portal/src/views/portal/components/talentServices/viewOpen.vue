<template>
  <contentView :isEdit="isEdit" :form="form"></contentView>
</template>
<script>
import { getToken } from '@/utils/auth'
import contentView from './content'
import {decrypt, sm4Dncrypt} from "@/utils/encrypt";
import {getIdNum, getPhoneNum} from "@/api/login";
export default {
  name: 'talentServiceOpen',
  components: {
    contentView
  },
  mounted() {
    this.init()
  },
  data(){
    return{
      isEdit:true,
      form: {
        SXMC:'',
        SXBM:'',
        NAME: '',
        IDCARD: '',
        KeyValue:'',
        PHONENUMBER: '',
        CONTENT: '',
        HANDLINGTIME: '',
        BMID: '',
        BMMC: '',
        token: '',
        readOnly:true,//true为编辑，false为查看
      }

    }
  },
  methods:{
    init(){
      //获取参数
      let fwInfo1 = this.$route.query.fwInfo;
      if(fwInfo1){
        // let fwInfo = JSON.parse(decrypt(this.$route.query.fwInfo))
        console.log('url参数：',fwInfo1)
        console.log('url参数空格替换+',fwInfo1.replace(/\s+/g,'+'))
        var text = decrypt(fwInfo1.replace(/\s+/g,'+'));
        console.log('解码后的json值',text)
        let fwInfo = JSON.parse(text)
        console.log('fwInfo对象',fwInfo)
        // let fwInfo = JSON.parse(decrypt(this.$route.query.fwInfo.replace(/\s+/g,'+')))
        // let fwInfo = JSON.parse(decrypt('HVZsAWWyiRFPZBHV4CsHUdjf9AXCWC91UTqTbDWgS8eFu9YJSNEC2P3lzN5I7uKIjfMCBgZpV5/Us43cJrqfs4 c77S5i0kaKDwidX6xO8xjBqhoPlX PPBjFH5pYbLrc6knAQNKE2c922Sfg6kKd3amaKbh0mMv9F8aI04/VBw='.replace(/\s+/g,'+')))
        this.form.SXMC =fwInfo.SXMC
        this.form.SXBM =fwInfo.SXBM
        this.form.CONTENT =fwInfo.XQNR
        this.form.HANDLINGTIME =fwInfo.YYSJ
        this.form.KeyValue =fwInfo.KEYVALUE
        this.isEdit=(fwInfo.readOnly==='false')
        this.ininData()
      }else{
        this.$router.go(-1)
      }
    },

    async ininData(){
      // let user=JSON.parse(sm4Dncrypt(this.$cache.local.get("portal_user")))
      let employee=JSON.parse(sm4Dncrypt(this.$cache.local.get("portal_employee")))

      this.form.BMID=employee.companyId
      this.form.BMMC=employee.coName
      this.form.token = getToken();
      await getPhoneNum().then(res=>{
        if(res.code==200&&res.data.phoneNum)
          this.form.PHONENUMBER=res.data.phoneNum
      })
      await getIdNum().then(res=>{
        if(res.code==200&&res.data.idNum)
          this.form.IDCARD=res.data.idNum
        this.form.NAME=res.data.realName
      })
    },

  },

}
</script>
