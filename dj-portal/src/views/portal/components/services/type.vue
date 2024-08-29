<template>
  <iFrame :iframeSrc="newSrc"></iFrame>
</template>

<script>
  import iFrame from "@/components/iFrame/full";
  export default {
    name: 'newDetail',
    components: { iFrame },
    mounted(){
    },
    data() {
      return {
        newSrc:process.env.VUE_APP_YSZJ_ADDRESS+"/rcfw/web/view/home.html",
      }
    },
    methods:{

    }

  }
</script>

<style scoped>
</style>
