<template>
  <contentView :isEdit="isEdit" :form="form"></contentView>
</template>
<script>
import { decrypt } from "@/utils/encrypt"
import { getPhoneNum,getIdNum } from '@/api/login'
import { sm4Dncrypt } from "@/utils/encrypt"
import { getToken } from '@/utils/auth'
import contentView from './content'
import { v4 as uuidv4 } from 'uuid';

export default {
  name: 'view',
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
        // CONTENT: '',
        HANDLINGTIME: '',
        BMID: '',
        BMMC: '',
        token: '',
      }

    }
  },
  methods:{
    init(){
      if(this.$route.query.fwInfo){
        let fwInfo = JSON.parse(decrypt(this.$route.query.fwInfo))
        this.form.SXMC =fwInfo.SXMC
        this.form.SXBM =fwInfo.SXBM
        this.form.keyValue=uuidv4()
      }else{
        this.$router.go(-1)
      }
      this.ininData()
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
