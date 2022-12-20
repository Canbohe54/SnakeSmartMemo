<template>
  <el-container
    direction="horizonal"
    v-loading="loading"
    :element-loading-text="publicLoadingText"
  >
    <el-container id="sidePane">
      <el-empty class="empty"></el-empty>
    </el-container>
    <el-container direction="vertical">
      <el-row class="tools">
        <el-button icon="el-icon-finished" @click="sidePaneVisual"
          >{{ openCloseSide }}事件列表</el-button
        >
        <el-button icon="el-icon-document-checked" @click="handleEventDealer()"
          >智能事件识别</el-button
        >
        <el-button icon="el-icon-microphone" @click="recoderVisible = true"
          >语音输入</el-button
        >
        <el-upload
          class="openLocal"
          action="#"
          :show-file-list="false"
          :http-request="httpRequestLocalFile"
        >
          <el-button icon="el-icon-monitor">从本地打开</el-button>
        </el-upload>
        <el-button icon="el-icon-download" @click="handleSaveLocalDialog()"
          >保存到本地</el-button
        >
        <el-button
          icon="el-icon-more"
          @click="moreButton=(moreButton==true?false:true) "
          ></el-button
        >
      </el-row>
      <transition name="el-fade-in-linear">
      <el-row class="tools" v-show="moreButton">
        <el-button
          icon="el-icon-link"
          @click="openLinkVisible = true"
          >从分享链接打开</el-button
        >
        <el-button
          icon="el-icon-share"
          :disabled="!hasLogin"
          @click="handleShare()"
          >分享</el-button
        >
        <el-button icon="el-icon-paperclip" @click="log()" :disabled="!hasLogin"
          >从云端打开</el-button
        >
        <el-button
          icon="el-icon-upload"
          :disabled="!hasLogin"
          @click="handleUploadDialog()"
          >上传到云端</el-button
        >
      </el-row>
    </transition>
      <!-- <el-button @click="formtoHtml">转换为Html样式</el-button> -->
      <div class="textEditor">
        <vue-html5-editor
          :content="ruleForm.content"
          v-model="ruleForm.content"
          show-module-name="true"
          @change="updateData"
        ></vue-html5-editor>
      </div>
      <el-dialog
        title="语音识别"
        :visible.sync="recoderVisible"
        width="30%"
        :modal="false"
        :show-close="false"
        :destroy-on-close="true"
        :close-on-click-modal="false"
        @open="handleRecoderOpen()"
        @close="handleRecoderClose()"
        v-loading="recogLoading"
        element-loading-text="正在识别"
      >
        <el-button
          :icon="recoderStatsPic"
          @click="handleRecode()"
          :disabled="recoded"
          >{{ recorderStats }}录音</el-button
        >
        <el-button
          icon="el-icon-headset"
          :disabled="!recoded"
          @click="handleRecoderPlay()"
          >试听</el-button
        >
        <el-button
          icon="el-icon-refresh"
          :disabled="!recoded"
          @click="handleRecognizer()"
          >转换</el-button
        >
        <el-button icon="el-icon-close" @click="handleRecoderExit()"
          >取消</el-button
        >
      </el-dialog>
      <el-dialog
        title="保存到本地"
        :visible.sync="saveLocalVisible"
        width="30%"
        :modal="false"
        :destroy-on-close="true"
        :close-on-click-modal="false"
      >
        <el-form
          :model="saveLocalruleForm"
          :rules="saveLocalrules"
          ref="saveLocalruleForm"
          class="demo-ruleForm"
        >
          <el-form-item label="" prop="localFileName"
            ><el-input
              class="saveDialogInput"
              v-model="saveLocalruleForm.localFileName"
              auto-complete="off"
              placeholder="请输入文件名"
            ></el-input
          ></el-form-item>
          <el-button icon="el-icon-download" @click="handleSaveLocal()"
            >保存</el-button
          >
        </el-form>
      </el-dialog>
      <el-dialog
        title="上传到云端"
        :visible.sync="uploadVisible"
        width="30%"
        :modal="false"
        :destroy-on-close="true"
        :close-on-click-modal="false"
        v-loading="uploadLoading"
        element-loading-text="正在上传"
      >
        <el-form
          :model="uploadruleForm"
          :rules="uploadrules"
          ref="uploadruleForm"
          class="demo-ruleForm"
        >
          <el-form-item label="" prop="uploadFileName"
            ><el-input
              class="saveDialogInput"
              v-model="uploadruleForm.uploadFileName"
              auto-complete="off"
              placeholder="请输入文件名"
            ></el-input
          ></el-form-item>
          <el-button icon="el-icon-upload2" @click="handleUpload()"
            >上传</el-button
          >
        </el-form>
      </el-dialog>
      <el-dialog
        title="从分享链接中打开"
        :visible.sync="openLinkVisible"
        width="30%"
        :modal="false"
        :destroy-on-close="true"
        :close-on-click-modal="false"
        v-loading="openLinkLoading"
        element-loading-text="正在同步内容"
      >
        <el-form
          :model="openLinkruleForm"
          :rules="openLinkrules"
          ref="openLinkruleForm"
          class="demo-ruleForm"
        >
          <el-form-item label="" prop="openLinkFileName"
            ><el-input
              class="saveDialogInput"
              v-model="openLinkruleForm.openLinkFileName"
              auto-complete="off"
              placeholder="请粘贴或输入分享链接"
            ></el-input
          ></el-form-item>
          <el-button icon="el-icon-connection" @click="handleOpenLink()"
            >打开</el-button
          >
        </el-form>
      </el-dialog>
      <el-dialog
        title="分享成功"
        :visible.sync="sharedVisible"
        width="30%"
        :modal="false"
        :destroy-on-close="true"
        :close-on-click-modal="false"
      >
        <el-input
          class="saveDialogInput"
          v-model="shareLink"
          auto-complete="off"
        >
          <template slot="prepend">分享地址:</template>
        </el-input>
        <el-button icon="el-icon-copy-document" @click="handleCopyShareLink()"
          >复制分享链接</el-button
        >
      </el-dialog>
      <el-backtop></el-backtop>
    </el-container>
  </el-container>
</template>
<script>
import Recorder from "js-audio-recorder";
import "../components/recoder.js";
export default {
  name: "NotepadView",
  components: {},
  data() {
    return {
      moreButton:false,
      loading: false,
      hasLogin: false,
      openCloseSide: "关闭",
      recoderVisible: false,
      recorderStats: "开始",
      recoderStatsPic: "el-icon-mic",
      recoded: false,
      recogLoading: false,
      uploadLoading: false,
      openLinkLoading: false,
      saveLocalVisible: false,
      uploadVisible: false,
      sharedVisible: false,
      openLinkVisible: false,
      publicLoadingText: "",
      ruleForm: {
        content: "",
      },
      saveLocalruleForm: {
        localFileName: "",
      },
      saveLocalrules: {
        localFileName: [
          { required: true, message: "请输入文件名", trigger: "blur" },
        ],
      },
      uploadruleForm: {
        uploadFileName: "",
        uploadFile: "",
      },
      uploadrules: {
        uploadFileName: [
          { required: true, message: "请输入文件名", trigger: "blur" },
        ],
      },
      openLinkruleForm: {
        openLinkFileName: "",
      },
      openLinkrules: {
        openLinkFileName: [
          { required: true, message: "请输入分享链接", trigger: "blur" },
        ],
      },
      shareLink: "",
    };
  },
  methods: {
    updateData: function (data) {
      this.ruleForm.content = data;
      data = this.ruleForm.content;
    },
    log() {
      console.log(this.ruleForm.content);
    },
    formtoHtml() {
      document.execCommand(""); //改进之处：可以转换为html文档
    },
    // 点击复制到剪贴板函数
    copyToClipboard(content) {
      //window.clipboardData的作用是在页面上将需要的东西复制到剪贴板上，
      //提供了对于预定义的剪贴板格式的访问，以便在编辑操作中使用。
      if (window.clipboardData) {
        /*
          window.clipboardData有三个方法:
        （1）clearData(sDataFormat) 删除剪贴板中指定格式的数据。sDataFormat:"text","url"
        （2）getData(sDataFormat) 从剪贴板获取指定格式的数据。 sDataFormat:"text","url"
        （3）setData(sDataFormat, sData) 给剪贴板赋予指定格式的数据。返回 true 表示操作成功。
          */
        window.clipboardData.setData("text", content);
      } else {
        (function (content) {
          //oncopy 事件在用户拷贝元素上的内容时触发。
          document.oncopy = function (e) {
            e.clipboardData.setData("text", content);
            e.preventDefault(); //取消事件的默认动作
            document.oncopy = null;
          };
        })(content);
        //execCommand方法是执行一个对当前文档/当前选择/给出范围的命令。
        //'Copy':将当前选中区复制到剪贴板。
        document.execCommand("Copy");
      }
    },
    sidePaneVisual() {
      //开启/关闭侧边栏
      let sidePaneClass = document.getElementById("sidePane");
      if (sidePaneClass.getAttribute("style")) {
        sidePaneClass.removeAttribute("style");
        this.openCloseSide = "关闭";
      } else {
        sidePaneClass.setAttribute("style", "display: none!important;");
        this.openCloseSide = "打开";
      }
    },
    handleRecoderOpen() {
      //console.log("打开录音窗口");
      this.recorderStats = "开始";
      this.recoderStatsPic = "el-icon-mic";
      this.recoded = false;
    },
    handleRecoderClose() {
      //console.log("销毁实例");
      this.recorder.destroy(); // 毁实例
      this.timer = null;
    },
    handleRecode() {
      if (this.recorderStats == "开始") {
        this.recorderStats = "结束";
        this.recoderStatsPic = "el-icon-check";
        this.recorder = new Recorder({
          sampleBits: 16, // 采样位数，支持 8 或 16，默认是16
          sampleRate: 16000, // 采样率，支持 11025、16000、22050、24000、44100、48000，根据浏览器默认值，我的chrome是48000
          numChannels: 1, // 声道，支持 1 或 2， 默认是1
        });
        Recorder.getPermission().then(
          () => {
            //console.log("开始录音");
            this.recorder.start(); // 开始录音
          },
          (error) => {
            this.$message({
              message: "请先允许该网页使用麦克风",
              type: "info",
            });
            console.log(`${error.name} : ${error.message}`);
          }
        );
      } else if (this.recorderStats == "结束") {
        this.recorderStats = "完成";
        this.recoded = true;
        //console.log("停止录音");
        this.recorder.stop();
      }
    },
    handleRecoderPlay() {
      this.recorder.play();
    },
    handleRecognizer() {
      //console.log("开始转换");
      if (this.recorder == null || this.recorder.duration === 0) {
        this.$message({
          message: "请先录音",
          type: "error",
        });
        return false;
      }
      this.recogLoading = true;
      this.recorder.pause(); // 暂停录音
      this.timer = null;
      //console.log('上传录音')// 上传录音

      const formData = new FormData();
      const blob = this.recorder.getWAVBlob(); // 获取wav格式音频数据
      // 此处获取到blob对象后需要设置fileName满足当前项目上传需求，其它项目可直接传把blob作为file塞入formData
      const newbolb = new Blob([blob], { type: "audio/wav" });
      const fileOfBlob = new File([newbolb], new Date().getTime() + ".wav");
      formData.append("file", fileOfBlob);
      //const url = window.URL.createObjectURL(fileOfBlob)
      //this.src = url
      //注释内容：将录音保存到前端服务器上
      this.$axios.post("ssm/recognize", formData).then((resp) => {
        //console.log(resp);
        this.recogLoading = false;
        if (resp.data.statusMsg == "success") {
          if (resp.data.events == "") {
            this.$message({
              message: "蛇蛇没听清呢，请再试一遍",
              type: "error",
            });
            this.handleRecoderExit();
            return;
          }
          if (resp.data.events == "$Error!$") {
            this.$message({
              message: "识别错误，请稍后再试",
              type: "error",
            });
            this.handleRecoderExit();
            return;
          }
          this.ruleForm.content += resp.data.events;
          this.$message({
            message: "转换成功",
            type: "success",
          });
          this.handleRecoderExit();
        }
      });
      //this.recorder.destroy();
      // const axios = require('axios')
      // axios.post(url, formData).then(res => {
      //   console.log(res.data.data[0].url)
      // })
    },
    handleRecoderExit() {
      this.handleRecoderClose();
      this.recoderVisible = false;
    },
    handleSaveLocalDialog() {
      if (this.ruleForm.content == "" || this.ruleForm.content == "<br>") {
        this.$message({
          message: "没有编写内容，保存失败",
          type: "error",
        });
        return;
      }
      this.saveLocalVisible = true;
    },
    handleSaveLocal() {
      this.$refs.saveLocalruleForm.validate((valid) => {
        if (valid) {
          //console.log("valid");
          let fileName = this.saveLocalruleForm.localFileName;
          const formData = new FormData();
          const blob = this.ruleForm.content; // 获取html格式文本数据
          // 此处获取到blob对象后需要设置fileName满足当前项目上传需求，其它项目可直接传把blob作为file塞入formData
          const newbolb = new Blob([blob], { type: "text/html" });
          const fileOfBlob = new File([newbolb], fileName + ".html");
          formData.append("file", fileOfBlob);
          let url = window.URL.createObjectURL(fileOfBlob);
          let a = document.createElement("a");
          a.style.display = "none";
          a.href = url;
          a.download = fileOfBlob.name;
          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);
          window.URL.revokeObjectURL(url);
          this.$message({
            message: "已开始下载",
            type: "success",
          });
        }
      });
      this.saveLocalVisible = false;
    },
    // handleChange(file, fileList) {
    //   let localFile = file;
    //   const formData = new FormData();
    //   formData.append(file.name,file);
    //   console.log(file);
    // },
    httpRequestLocalFile(data) {
      //console.log(data.file);
      this.$confirm("此操作将覆盖原记事本数据（真的很久）, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.loading = true;
          this.publicLoadingText = "正在读取本地文件";
          let reader = new FileReader();
          const theThis = this;
          reader.readAsText(data.file);
          reader.onload = function () {
            theThis.ruleForm.content = this.result;
          };
          this.loading = false;
          this.publicLoadingText = "";
          this.$message({
            type: "success",
            message: "读取本地文件成功!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消读取本地文件",
          });
        });
    },
    handleEventDealer() {
      this.loading = true;
      this.publicLoadingText = "正在识别";
      const params = new URLSearchParams();
      params.append("text", this.ruleForm.content);
      this.$axios.post("ssm/event", params).then((resp) => {
        console.log(resp.data);
      });
      // const formData = new FormData();
      // const blob = this.ruleForm.content; // 获取html格式文本数据
      // // 此处获取到blob对象后需要设置fileName满足当前项目上传需求，其它项目可直接传把blob作为file塞入formData
      // const newbolb = new Blob([blob], { type: "text/html" });
      // const fileOfBlob = new File([newbolb], new Date().getTime() + ".html");
      // formData.append("file", fileOfBlob);
      // let url = window.URL.createObjectURL(fileOfBlob);
      // this.$axios.post("ssm/event", formData).then((resp) => {
      //   console.log(resp.data);
      // });
      // window.URL.revokeObjectURL(url);
      this.loading = false;
      this.publicLoadingText = "";
    },
    handleUploadDialog() {
      if (this.ruleForm.content == "" || this.ruleForm.content == "<br>") {
        this.$message({
          message: "没有编写内容，上传失败",
          type: "error",
        });
        return;
      }
      this.uploadVisible = true;
    },
    handleUpload() {
      this.uploadLoading = true;
      let id = this.$store.getters.getUser.id;
      let token = this.$store.getters.getToken;
      this.$refs.uploadruleForm.validate((valid) => {
        if (valid) {
          //console.log("valid");
          let fileName = this.uploadruleForm.uploadFileName;
          const formData = new FormData();
          this.uploadruleForm.uploadFile = this.ruleForm.content; //将要上传的保存起来
          const blob = this.uploadruleForm.uploadFile; // 获取html格式文本数据
          // 此处获取到blob对象后需要设置fileName满足当前项目上传需求，其它项目可直接传把blob作为file塞入formData
          const newbolb = new Blob([blob], { type: "text/html" });
          const fileOfBlob = new File([newbolb], fileName + ".html");

          // let url = window.URL.createObjectURL(fileOfBlob);
          // let a = document.createElement("a");
          // a.style.display = "none";
          // a.href = url;
          // a.download = fileOfBlob.name;
          // document.body.appendChild(a);
          // a.click();
          // document.body.removeChild(a);
          // window.URL.revokeObjectURL(url);
          const params = new URLSearchParams();
          formData.append("id", id);
          formData.append("token", token);
          formData.append("file", fileOfBlob);
          formData.append("filename", fileName + ".html");
          this.$axios.post("ssm/users/upload", formData).then((resp) => {
            //console.log(resp.data);
            if (resp.data.statusMsg == "success") {
              this.$message({
                message: "上传成功",
                type: "success",
              });
            } else if (resp.data.statusMsg == "TokenExpirationTimeException") {
              this.$message({
                message: "用户身份过期，请重新登录！",
                type: "error",
              });
              this.$store.commit("REMOVE_INFO");
              this.$router.push("/loading");
            } else {
              //console.log(resp);
              this.$message({
                message: "上传失败，请稍后再试",
                type: "error",
              });
            }
          });
        }
      });
      this.uploadLoading = false;
      this.uploadVisible = false;
    },
    handleShare() {
      if (this.ruleForm.content == "" || this.ruleForm.content == "<br>") {
        this.$message({
          message: "没有编写内容，分享失败",
          type: "error",
        });
        return;
      }
      if (this.ruleForm.content != this.uploadruleForm.uploadFile) {
        this.$message({
          message: "编写的内容发生改变，请重新上传！",
          type: "error",
        });
        return;
      }
      this.loading = true;
      this.publicLoadingText = "正在创建分享链接";
      let id = this.$store.getters.getUser.id;
      let token = this.$store.getters.getToken;
      const params = new URLSearchParams();
      params.append("id", id);
      params.append("token", token);
      params.append("file_name", this.uploadruleForm.uploadFileName + ".html");
      this.$axios.post("ssm/users/share", params).then((resp) => {
        if (resp.data.statusMsg == "success") {
          console.log(resp);
          this.loading = false;
          this.publicLoadingText = "";
          this.sharedVisible = true;
          this.shareLink = resp.data.fileUrl;
        } else if (resp.data.statusMsg == "TokenExpirationTimeException") {
              this.$message({
                message: "用户身份过期，请重新登录！",
                type: "error",
              });
              this.$store.commit("REMOVE_INFO");
              this.$router.push("/loading");
            } else {
              this.$message({
                message: "创建分享链接失败，请稍后再试",
                type: "error",
              });
            }
      });
    },
    handleCopyShareLink() {
      this.copyToClipboard(this.shareLink);
      this.$message({
        message: "复制成功！",
        type: "success",
      });
    },
    handleOpenLink() {
      this.$refs.openLinkruleForm.validate((valid) => {
        if (valid) {
          this.$confirm(
            "此操作将覆盖原记事本数据（真的很久）, 是否继续?",
            "提示",
            {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            }
          )
            .then(() => {
              this.$axios
                .post(this.openLinkruleForm.openLinkFileName)
                .then((resp) => {
                  if ((resp.statusText = "OK")) {
                    this.ruleForm.content = resp.data;
                    this.$message({
                      type: "success",
                      message: "读取分享文件成功！"
                    })
                  }else{
                    this.$message({
                type: "error",
                message: "从分享链接打开失败，请稍后再试",
              });
                  }
                });
            })
            .catch(() => {
              this.$message({
                type: "info",
                message: "已取消从分享链接中打开",
              });
            });
            this.openLinkVisible = false;
        }
      });
    },
  },
  created() {
    this.recorder = new Recorder({
      sampleBits: 16, // 采样位数，支持 8 或 16，默认是16
      sampleRate: 16000, // 采样率，支持 11025、16000、22050、24000、44100、48000，根据浏览器默认值，我的chrome是48000
      numChannels: 1, // 声道，支持 1 或 2， 默认是1
    });
    if (this.$store.getters.getUser.username) {
      this.hasLogin = true;
    } else {
      this.hasLogin = false;
    }
  },
};
</script>
<style>
.tools {
  margin: 0 0 20px 0;
}
#sidePane {
  background-color: powderblue;
  height: auto;
  margin: 0 10px 0 0;
  border-radius: 5px;
}
/* .saveDialogTitle {
  font: 17px "Microsoft Yahei";
  margin: 0 0 20px 10px;
} */
.saveDialogInput {
  margin: 0 0 10px 0;
}
.openLocal {
  display: inline;
  margin: 0 10px;
}
.empty {
  margin: auto;
}
</style>