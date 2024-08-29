// 公共类型声明
import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
// 当前[信息模块]分类声明
import { ChatCategoryEnum,ChatCategoryEnumName } from '../../index.d'

export const BarProgressConfig: ConfigType = {
    key: 'BarProgress',
    chartKey: 'VBarProgress',
    conKey: 'VCBarProgress',
    title: '横向进度柱状图',
    category: ChatCategoryEnum.BAR,
    categoryName: ChatCategoryEnumName.BAR,
    package: PackagesCategoryEnum.CHARTS,
    chartFrame: ChartFrameEnum.ECHARTS,
    image: 'pictorialBar.png'
}
