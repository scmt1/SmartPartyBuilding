<template>
<div id="floatingAd" class="floatingAd">
  <template v-for="(item, index) in list">
    <FloatingAdItem :ad="item" :key="index" :step="step" :delay="delay" :isLinkClosed="isLinkClosed" @close="$emit('close', item)" />
  </template>
</div>
</template>

<script>
import FloatingAdItem from './FloatingAdItem.vue'
export default {
  components: {
    FloatingAdItem,
  },
  name: 'floating-ad',
  props: {
    step: {
      type: Number,
      default: 1
    },
    delay: {
      type: Number,
      default: 50
    },
    isLinkClosed: {
      type: Boolean,
      default: false
    },
    ad: {
      type: Array,
      required: true
    }
  },
  components: {
    FloatingAdItem
  },
  data() {
    return {
      list: []
    }
  },
  created() {
    this.list = this.ad.map(item => {
      return item;
    })
  }
}
</script>

<style>
</style>
