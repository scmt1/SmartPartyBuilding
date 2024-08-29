// 公共类型声明
import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
// 当前[信息模块]分类声明
import { ChatCategoryEnum, ChatCategoryEnumName } from '../../index.d'

export const PieDashboardConfig: ConfigType = {
    key: 'PieDashboard',
    chartKey: 'VPieDashboard',
    conKey: 'VCPieDashboard',
    title: '仪表盘',
    category: ChatCategoryEnum.PIE,
    categoryName: ChatCategoryEnumName.PIE,
    package: PackagesCategoryEnum.CHARTS,
    chartFrame: ChartFrameEnum.COMMON,
    image: 'dashboard.png'
}
