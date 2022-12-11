<template>
  <div>
    <el-container>
      <el-header>
        <div class="snakelogo"></div>
        <div class="loginText">登录</div>
        <!-- <img class="mlogo" src="https://www.markerhub.com/dist/images/logo/markerhub-logo.png" alt=""> -->
      </el-header>
      <el-main>
        <el-form
          :model="ruleForm"
          :rules="rules"
          ref="ruleForm"
          class="demo-ruleForm"
        >
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
          <el-form-item>
            <el-button type="primary" @click="submitForm()" class="submit"
              >立即登录</el-button
            >
            <!-- <el-button @click="resetForm('ruleForm')">重置</el-button> -->
            <!-- <el-link href="#" target="_self" class="regLink">还没有账户？点击注册</el-link> -->
            <router-link to="/Register" class="regLink"
              >还没有账户？点击注册</router-link
            >
          </el-form-item>
        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      ruleForm: {
        id: "",
        password: "",
      },
      rules: {
        id: [
          { required: true, message: "请输入账户ID", trigger: "blur" },
          {
            min: 4,
            max: 15,
            message: "长度在 4 到 15 个字符",
            trigger: "blur",
          },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
      },
    };
  },
  methods: {
    submitForm() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          let id = this.ruleForm.id;
          let password = this.ruleForm.password;
          const params = new URLSearchParams();
          params.append("id", id);
          params.append("password", password);
          this.$axios.post("ssm/users/login", params).then((resp) => {
            if (resp.data.statusMsg == "success") {
              this.$message({
                message: "登录成功",
                type: "success",
              });
              //token = resp.data.token;
              let userInfo = resp.data.userInfo;
              //this.$store.commit("SET_TOKEN",token)
              this.$store.commit("SET_USERINFO", userInfo)
              this.$router.push({
                path: "/",
              });
            } else {
              this.$message({
                message: "登录失败",
                type: "error",
              });
            }
            console.log(resp);
          });
        }
      });
    },
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
  margin: 0 10px 0 0;
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