// 公共类型声明
import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
// 当前[信息模块]分类声明
import { ChatCategoryEnum,ChatCategoryEnumName } from '../../index.d'

export const SunburstConfig: ConfigType = {
    key: 'Sunburst',
    chartKey: 'VSunburst',
    conKey: 'VCSunburst',
    title: '旭日图',
    category: ChatCategoryEnum.MORE,
    categoryName: ChatCategoryEnumName.MORE,
    package: PackagesCategoryEnum.CHARTS,
    chartFrame: ChartFrameEnum.ECHARTS,
    image: 'sunburst.png'
}
