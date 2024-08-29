<template>
  <iframe
    ref="myIframe"
    width="100%"
    :src="iframeSrc"
    frameborder="no"
    scrolling="auto"
  ></iframe>
</template>

<script>
  import ThirdParty from '@/orange-party/index.js';
  export default {
    name: 'fullIframe',
    props:{
      iframeSrc:String,
    },
    mixins: [ThirdParty],
    mounted(){
      // var a=decrypt("fRg/Snw7kygrVjwDGiTHaaJAmJluF4dU6MDbCSsvchw4Dq418gnLejv7GW7yTjChgfc8XudQiNyURbMlKKS4lJjzve+7iaEvNrDFzRUpqpQwZl6gbWtLhNMKilzIqbWZcvLQk1DHiYww8jcF3c7KdBAfFyJd5+UTUKcdbwYv8cXZcrV8SZNc632A0HuZ2NWGC6eTQayrTOpcrA/5BNW2BnG0P1CJLIdCrNd87wwdXA4uxrxLqyrP/5YKV1f2wbgrQCSNHaoLw3nDx5+/R/0JpUt3C+Lc7X8Iz5d8/t7IwKU=")
      // console.log("aaaaaa",a)
      /*const iframe = this.$refs.myIframe;
      /*const iframe = this.$refs.myIframe;
      iframe.onload = () => {
        console.log("iframeSrc",this.iframeSrc)
        console.log(iframe.contentWindow.document.body.scrollHeight + 'px')
        // iframe.style.height = (iframe.contentWindow.document.body.scrollHeight+100) + 'px';
        iframe.style.height ='1200px';
      };*/

    },
    data() {
      return {

      }
    },
    methods:{

    }

  }

</script>

<style scoped>
  iframe::-webkit-scrollbar{
    display: none;
  }
</style>
