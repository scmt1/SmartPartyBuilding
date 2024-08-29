<template>
    <!-- Echarts 全局设置 -->
    <global-setting :optionData="optionData"></global-setting>
</template>

<script setup lang="ts">
    import { PropType, watch } from 'vue'
    import { GlobalThemeJsonType } from '@/settings/chartThemes/index'
    import { GlobalSetting, CollapseItem, SettingItemBox, SettingItem } from '@/components/Pages/ChartItemSetting'

    const props = defineProps({
        optionData: {
            type: Object as PropType<GlobalThemeJsonType>,
            required: true
        }
    })
</script>
