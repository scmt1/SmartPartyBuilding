// 公共类型声明
import { ConfigType, PackagesCategoryEnum, ChartFrameEnum } from '@/packages/index.d'
// 当前[信息模块]分类声明
import { ChatCategoryEnum,ChatCategoryEnumName } from '../../index.d'

export const LoadingConfig: ConfigType = {
    key: 'Loading',
    chartKey: 'VLoading',
    conKey: 'VCLoading',
    title: '加载动画',
    category: ChatCategoryEnum.MORE,
    categoryName: ChatCategoryEnumName.MORE,
    package: PackagesCategoryEnum.DECORATES,
    chartFrame: ChartFrameEnum.COMMON,
    image: 'loading.png'
}
