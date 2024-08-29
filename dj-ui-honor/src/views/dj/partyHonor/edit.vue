<template>
    <el-dialog title="提示" :visible.sync="dialogVisible" width="560px">
        <el-form ref="form" :rules="rules" :model="form" label-width="110px" style="text-align: center; padding: 0 20px 0 20px;">
            <div style="display: inline-block; margin: auto;">
                <el-form-item prop="honorType" label="荣誉类型：">
                    <el-select style="width: 300px;" v-model="form.honorType" clearable @change="honorTypeChange" placeholder="请输入选择荣誉类型">
                        <el-option v-for="item in honorTypeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item prop="partyMemberId" label="获奖党员：" v-if="form.honorType == 2">
                    <el-select style="width: 300px;" v-model="form.partyMemberId" filterable clearable placeholder="请选择获奖党员">
                        <el-option v-for="item in partyMemberList" :key="item.id" :label="item.realName + ' - ' + item.idcard" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="获奖照片：">
                    <el-upload
                        class="avatar-uploader"
                        :action="'#'"
                        :show-file-list="false"
                        :headers="{Authorization: $cookie.get('Authorization_party_vp')}"
                        :before-upload="beforeImageUpload">
                        <img v-if="form.imagePath" :src="base+form.imagePath" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>

                <el-form-item prop="honorName" label="荣誉名称：">
                    <el-input style="width: 300px;" maxlength="150" v-model="form.honorName" placeholder="荣誉名称" autocomplete="off"></el-input>
                </el-form-item>

                <el-form-item prop="honorClass" label="荣誉级别：">
                    <el-select style="width: 300px;" v-model="form.honorClass" clearable @change="honorTypeChange" placeholder="请输入选择荣誉类型">
                        <el-option v-for="item in honorClassOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
                    </el-select>
                </el-form-item>

                <el-form-item prop="getYear" label="获奖时间：">
                    <el-date-picker style="width: 300px;" v-model="form.getYear" type="year" placeholder="选择年份"></el-date-picker>
                </el-form-item>
            </div>

        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </span>
        <cropper ref="cropper" :auto-crop-width="800" :auto-crop-height="536" :fixed-number="[1, 0.67]" @cropperResult="uploadCropperImg"></cropper>
    </el-dialog>
</template>

<script>
import {getPartMemberListByDeptId} from "@/api/partyMember";
import {addTzPartyHonor, updateTzPartyHonor, getTzPartyHonorById} from "@/api/TzPartyHonor";
import {uploadImages} from "@/api/attachFile";
import cropper from "@/views/dj/components/cropper.vue";
import util from "@/libs/util";
export default {
    name: "add",
    components: {
        cropper
    },
    data() {
        return {
            base: util.nginxUrl,
            dialogVisible: false,
            form: {
                id: '',
                deptId: '',
                partyMemberId: '',
                partyMemberName: '',
                honorName: '',
                honorType: '',
                imagePath: '',
                honorClass: '',
                getYear: ''
            },
            partyMemberList: [],
            honorTypeOptions: [
                {
                    label: '组织荣誉',
                    value: 1
                },
                {
                    label: '个人荣誉',
                    value: 2
                }
            ],
            honorClassOptions: [
                {
                    label: '国家级',
                    value: 1
                },
                {
                    label: '省级',
                    value: 2
                },
                {
                    label: '市级',
                    value: 3
                },
            ],
            rules: {
                honorType: [{ required: true, message: '请选择荣誉类型', trigger: 'blur' }],
                partyMemberId: [{ required: true, message: '请选择获奖党员', trigger: 'blur' }],
                honorName: [{ required: true, message: '请输入荣誉名称', trigger: 'blur' }],
                honorClass: [{ required: true, message: '请选择荣誉级别', trigger: 'blur' }],
                getYear: [{ required: true, message: '请选择获奖时间', trigger: 'blur' }],
            }
        }
    },
    methods: {
        init(id) {
            if (id) {
                getTzPartyHonorById(id).then(res => {
                    const result = res.data
                    if (result.code == '00000') {
                        this.form = result.data
                    } else {
                        this.$Message.error(result.msg)
                    }
                })
            } else {
                this.reset()
            }
            this.dialogVisible = true
        },
        honorTypeChange() {
            if (this.form.honorType == 2) {
                let userInformation = JSON.parse(window.localStorage.getItem("userInformation"))
                getPartMemberListByDeptId(userInformation.deptId + '').then(res => {
                    const result = res.data
                    if (result.code == '00000') {
                        this.partyMemberList = result.data
                        this.$forceUpdate()
                    }
                })
            }
        },
        beforeImageUpload(file) {
            const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
            const isLt5M = file.size / 1024 / 1024 < 5

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG/PNG 格式!')
            }
            if (!isLt5M) {
                this.$message.error('上传头像图片大小不能超过 5MB!')
            }

            this.$refs.cropper.init(file)
        },
        uploadCropperImg(file) {
            let reader = new FileReader()
            reader.readAsDataURL(file)
            reader.onload = () => {
                uploadImages({imgUrl: reader.result}).then(res => {
                    this.form.cover = res.data.data
                    this.form.imagePath = res.data.data
                })
            }
        },
        save() {
            this.$refs.form.validate((valid) => {
                if (valid) {

                    if (this.form.imagePath == '') {
                        this.$message.info('请上传图片')
                        return
                    }

                    let data = {
                        tzPartyHonor: this.form
                    }
                    let userInformation = JSON.parse(window.localStorage.getItem("userInformation"))
                    data.tzPartyHonor.deptId = userInformation.deptId

                    if (this.form.id == '') {
                        addTzPartyHonor({ data: data }).then(res =>{
                            const result = res.data
                            if (result.code == '00000') {
                                this.$message.success('保存成功')
                                this.$emit('refresh')
                                this.dialogVisible = false
                            } else {
                                this.$message.error(result.msg)
                            }
                        })
                    } else {
                        updateTzPartyHonor({ data: data }).then(res =>{
                            const result = res.data
                            if (result.code == '00000') {
                                this.$message.success('保存成功')
                                this.$emit('refresh')
                                this.dialogVisible = false
                            } else {
                                this.$message.error(result.msg)
                            }
                        })
                    }
                }
            })
        },
        reset() {
            this.form.deptId = ''
            this.form.partyMemberId = ''
            this.form.partyMemberName = ''
            this.form.honorName = ''
            this.form.honorType = ''
            this.form.imagePath = ''
            this.form.honorClass = ''
            this.form.getYear = ''
        }
    }
}
</script>

<style scoped lang="scss">
.avatar-uploader ::v-deep .el-upload {
    border: 1px dashed #d9d9d9 !important;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
}

.avatar-uploader ::v-deep .el-upload:hover {
    border-color: #409EFF;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
}

.avatar {
    width: 178px;
    height: 178px;
    display: block;
    object-fit: cover;
}
</style>
