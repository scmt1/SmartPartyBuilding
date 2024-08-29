// 公共类型声明
import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
// 当前[信息模块]分类声明
import { ChatCategoryEnum,ChatCategoryEnumName } from '../../index.d'

export const WaterPoloAConfig: ConfigType = {
    key: 'WaterPoloA',
    chartKey: 'VWaterPoloA',
    conKey: 'VCWaterPoloA',
    title: '多线条水球图',
    category: ChatCategoryEnum.MORE,
    categoryName: ChatCategoryEnumName.MORE,
    package: PackagesCategoryEnum.CHARTS,
    chartFrame: ChartFrameEnum.COMMON,
    image: 'waterPolo2.png'
}
