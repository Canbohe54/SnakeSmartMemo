<template>
  <el-container id="app" direction="vertical">
    <el-header class="navHeader">
      <!-- <el-row>
        <el-col> </el-col>
        <el-col>
          <div class="logo"></div>
          <el-menu
            default-active="notepad"
            class="el-menu-demo"
            mode="horizontal"
            router
          >
            <el-menu-item index="notepad" route="/" class="notepad"
              >笔记本</el-menu-item
            >
            <el-menu-item index="login" route="/login" class="loginAndReg"
              >登录/注册</el-menu-item
            >
          </el-menu>
        </el-col>
      </el-row> -->
      <!-- <el-row>
        <span class="logo"></span>
        <span class="title">蛇蛇智能备忘录</span>
        <el-tabs v-model="activeName" @tab-click="handleClick" class="navTabs">
        <el-tab-pane label="备忘录" name="notepad"></el-tab-pane>
        <el-tab-pane :label=userName name="login"></el-tab-pane>
        <el-tab-pane v-show="hasLogin" label="登出" name="logout"></el-tab-pane>
        </el-tabs>
      </el-row> -->
      <el-row>
        <span class="logo"></span>
        <span class="title">蛇蛇智能备忘录</span>
        <el-menu :default-active=activeName class="el-menu-demo" mode="horizontal" @select="handleSelect">
          <el-menu-item index="notepad">备忘录</el-menu-item>
          <el-menu-item index="login" :disabled=hasLogin>{{userName}}</el-menu-item>
          <el-menu-item v-show="hasLogin" index="logout">退出</el-menu-item>
        </el-menu>
      </el-row>
    </el-header>
    <el-main>
      <router-view />
    </el-main>
  </el-container>
</template>
<script>
  export default {
    data() {
      return {
        userName: '登录/注册',
        activeName: (this.$router.currentRoute.name=='login'?'login':'notepad'),
        hasLogin: false
      };
    },
    methods: {
      handleClick(tab, event) {
        if(tab.name=="login"){
          if(!this.hasLogin){
            this.$router.push("/login");
          }
        }
        if(tab.name=="notepad"){
          this.$router.push("/");
        }
        if(tab.name=="logout"){
          this.$store.commit("REMOVE_INFO");
          this.userName='登录/注册';
          this.hasLogin = false;
        }
      },
      handleSelect(key, keyPath){
        console.log(key)
        console.log(keyPath)
        if(key=="login"){
          if(!this.hasLogin){
            this.$router.push("/login");
          }
          console.log(this.hasLogin)
        }
        if(key=="notepad"){
          this.$router.push("/");
        }
        if(key=="logout"){
          this.$confirm('确定要退出账户吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$message({
            type: 'success',
            message: '退出成功'
          });
          this.$store.commit("REMOVE_INFO");
          this.userName='登录/注册';
          this.hasLogin = false;
          this.$router.push("/loading");
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消退出'
          });          
        });
        }
      }
    },
    updated(){
        if(this.$store.getters.getUser.username){
          this.userName=this.$store.getters.getUser.username;
          this.hasLogin=true;
        }
        this.activeName=(this.$router.currentRoute.name=='login'?'login':'notepad')
      }
  };
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  width: 100%;
  height: 100%;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}

.notepad {
  margin: 0 0 0 120px !important;
}
.loginAndReg {
  float: right !important;
  margin: 0 20px 0 0 !important;
}
.navHeader {
  background-color: whitesmoke;
  border-radius: 5px;
}

.logo {
  display: flex;
  float: left;
  height: 40px;
  width: 40px;
  margin: 10px 10px;
  background: url("assets/snake_emoji.png") no-repeat center center;
  background-size: contain;
}
.title {
  display: flex;
  float: left;
  margin: 10px 10px;
  font: normal 400 20px/40px "Microsoft YaHei";
}
.navHeader .el-tabs {
  float: right;
  margin: 0 30px;
}
.navHeader .el-tabs__item {
  padding: 10px 30px 50px;
  font: normal 400 18px/40px "Microsoft YaHei";
}

.navHeader .el-menu {
  float: right;
  margin: 0 30px;
  background: none;
}
.navHeader .el-menu--horizontal>.el-menu-item {
  padding: 10px 30px 50px;
  font: normal 400 18px/40px "Microsoft YaHei";
}
/* .navHeader .el-menu--horizontal>.el-menu-item.is-active {
  background: rgba(255, 255, 255, 0.5);
} */
/* .el-menu-demo {
  display: inline-block;
  height: max-content;
}
.el-menu.el-menu--horizontal {
  border-bottom: 0;
} */
</style>
