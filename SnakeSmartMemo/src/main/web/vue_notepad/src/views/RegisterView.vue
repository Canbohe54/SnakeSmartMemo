<template>
  <div>
    <el-container>
      <el-header>
        <div class="snakelogo"></div>
        <div class="loginText">注册</div>
        <!-- <img class="mlogo" src="https://www.markerhub.com/dist/images/logo/markerhub-logo.png" alt=""> -->
      </el-header>
      <el-main>
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          class="demo-ruleForm"
        >
          <el-form-item label="" prop="username">
            <el-input
              v-model="ruleForm.username"
              prefix-icon="el-icon-s-check"
              autocomplete="off"
              placeholder="请输入用户名"
            ></el-input>
          </el-form-item>
          <el-form-item label="" prop="id">
            <el-input
              v-model="ruleForm.id"
              prefix-icon="el-icon-user-solid"
              autocomplete="off"
              placeholder="请输入账户ID"
            ></el-input>
          </el-form-item>
          <el-form-item label="" prop="password">
            <el-input
              type="password"
              v-model="ruleForm.password"
              prefix-icon="el-icon-lock"
              autocomplete="off"
              placeholder="请输入密码"
            ></el-input>
          </el-form-item>
          <el-form-item label="" prop="passwordAgain">
            <el-input
              type="password"
              v-model="ruleForm.passwordAgain"
              prefix-icon="el-icon-lock"
              autocomplete="off"
              placeholder="请再次输入密码"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm()" class="submit"
              >立即注册</el-button
            >
            <!-- <el-button @click="resetForm('ruleForm')">重置</el-button> -->
            <!-- <el-link href="#" target="_self" class="regLink">已经有账户了？点击登录</el-link> -->
            <router-link to="/Login" class="regLink"
              >已经有账户了？点击登录</router-link
            >
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>
  
  <script>
export default {
  //在这里引入后端接口
  name: "Register",
  data() {
    let validatePassAgain = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.ruleForm.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };

    return {
      ruleForm: {
        id: "",
        username: "",
        password: "",
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        id: [
          { required: true, message: "请输入账户ID", trigger: "blur" },
          {
            min: 4,
            max: 15,
            message: "长度在 4 到 15 个字符",
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur",
          },
        ],
        passwordAgain: [
          {
            required: true,
            message: "两次密码必须一致",
            validator: validatePassAgain,
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    // submitForm(formName) {
    //   this.$refs[formName].validate((valid) => {
    //     if (valid) {
    //       const _this = this;
    //       this.$axios.post("/login", this.ruleForm).then((res) => {
    //         console.log(res.data);
    //         const jwt = res.headers["authorization"];
    //         const userInfo = res.data.data;
    //         // 把数据共享出去
    //         _this.$store.commit("SET_TOKEN", jwt);
    //         _this.$store.commit("SET_USERINFO", userInfo);
    //         // 获取
    //         console.log(_this.$store.getters.getUser);
    //         _this.$router.push("/blogs");
    //       });
    //     } else {
    //       console.log("error submit!!");
    //       return false;
    //     }
    //   });
    // },
    // resetForm(formName) {
    //   this.$refs[formName].resetFields();
    // }
    submitForm() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          let id = this.ruleForm.id;
          let user_name = this.ruleForm.username;
          let password = this.ruleForm.password;
          const params = new URLSearchParams();
          params.append("id", id);
          params.append("user_name", user_name);
          params.append("password", password);
          this.$axios.post("ssm/users/register", params).then((resp) => {
            if (resp.data == "register successfully") {
              this.$message({
                message: "注册成功",
                type: "success",
              });
              this.$refs.ruleForm.resetFields();
              this.$router.push({ path: "/login" });
            } else {
              this.$message({
                message: "注册失败",
                type: "error",
              });
            }
          });
        }
      });
    },
  },
};
</script>
  
  <style scoped>
.el-header,
.el-footer {
  /* background-color: #B3C0D1; */
  /* color: #333; */
  text-align: center;
  /* line-height: 180px; */
  height: 200px !important;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  /*background-color: #E9EEF3;*/
  color: #333;
  text-align: center;
  /* line-height: 160px;
    margin: 50px 0 0 0; */
}
body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

/* .mlogo {
      height: 60%;
      margin-top: 10px;
    } */
.snakelogo {
  display: block;
  height: 180px;
  background: url(../assets/snake_emoji.png) no-repeat center center;
}
.loginText {
  display: block;
  /* padding:0 0 170px 0; */
  /* line-height: 400px; */
  font: 20px "Microsoft YaHei";
  margin: 5px 0 0 0;
}
.regLink {
  text-decoration: none;
  color: #606266;
  font: 14px "Microsoft YaHei";
  display: block;
  height: 20px;
  line-height: 20px;
  margin: 0 0 0 0;
  float: right;
}
.regLink:hover {
  color: #5999fe;
}
.submit {
  position: relative;
  left: 75px;
}
.demo-ruleForm {
  max-width: 500px;
  margin: 0 auto;
}
</style>