<template>
    <el-dialog title="提示" :visible.sync="dialogVisible" width="660px">
        <el-form ref="form" :rules="rules" :model="form" label-width="110px" style="text-align: center; padding: 0 20px 0 20px;">
            <div style="display: inline-block; margin: auto;">
                <el-form-item prop="reason" label="驳回理由：">
                    <el-input style="width: 400px;" maxlength="500" :rows="5" show-word-limit v-model="form.reason" type="textarea"  placeholder="请输入驳回理由" autocomplete="off"></el-input>
                </el-form-item>
            </div>

        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </span>
    </el-dialog>
</template>

<script>
import {unPass} from "@/api/TzPartyHonor";;
export default {
    name: "unPass",
    data() {
        return {
            dialogVisible: false,
            form: {
                id: '',
                deptId: '',
                reason:''
            },
            rules: {
                reason: [{ required: true, message: '请输入驳回理由', trigger: 'blur' }]
            }
        }
    },
    methods: {
        init(id) {
            this.dialogVisible = true
            this.form.id = id
        },
        save() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    let data = {
                        tzPartyHonor: this.form
                    }
                    let userInformation = JSON.parse(window.localStorage.getItem("userInformation"))
                    data.tzPartyHonor.deptId = userInformation.deptId

                        unPass({ data: data }).then(res =>{
                            const result = res.data
                            if (result.code == '00000') {
                                this.$message.success('已驳回')
                                this.$emit('refresh')
                                this.reset()
                                this.dialogVisible = false
                            } else {
                                this.$message.error(result.msg)
                            }
                        })

                }
            })
        },
        reset() {
            this.form.deptId = ''
            this.form.reason = ''
            this.form.id = ''
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
