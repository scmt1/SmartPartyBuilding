import { echartOptionProfixHandle, PublicConfigClass } from '@/packages/public'
import { CreateComponentType } from '@/packages/index.d'
import cloneDeep from 'lodash/cloneDeep'
import {LoadingConfig} from "./index";

export const includes = []
export const option = {
    graphic: {
        elements: [
            {
                type: 'group',
                left: 'center',
                top: 'center',
                children: new Array(7).fill(0).map((val, i) => ({
                    type: 'rect',
                    x: i * 20,
                    shape: {
                        x: 0,
                        y: -20,
                        width: 10,
                        height: 40
                    },
                    style: {
                        fill: '#5470c6'
                    },
                    keyframeAnimation: {
                        duration: 1000,
                        delay: i * 200,
                        loop: true,
                        keyframes: [
                            {
                                percent: 0.5,  // 关键帧位置。0 为第一帧，1 为最后一帧
                                scaleY: 0.3,
                                easing: 'cubicIn'  // 上一个关键帧到这个关键帧运行时的缓动函数。可选
                            },
                            {
                                percent: 1,
                                scaleY: 1,
                                easing: 'cubicOut'
                            }
                        ]
                    }
                }))
            }
        ]
    }
}

export default class Config extends PublicConfigClass implements CreateComponentType
{
    public key = LoadingConfig.key
    public chartConfig = cloneDeep(LoadingConfig)
    public option = echartOptionProfixHandle(option, includes)
}
