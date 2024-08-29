<template>
  <iFrame :iframeSrc="newSrc"></iFrame>
</template>

<script>
  import { decrypt } from "@/utils/encrypt"
  import iFrame from "@/components/iFrame/full";
  export default {
    name: 'newDetail',
    components: { iFrame },
    mounted(){
      if(this.$route.query.newSrc){
        this.newSrc =decrypt(this.$route.query.newSrc)
        console.log(this.newSrc)
      }else{
        this.$route.back()
      }

    },
    data() {
      return {
        newSrc:"",
      }
    },
    methods:{

    }

  }
</script>

<style scoped>
</style>
