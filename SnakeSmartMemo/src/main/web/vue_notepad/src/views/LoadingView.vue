<template>
      <el-skeleton :rows="20" animated />
  </template>
  <script>
export default {
  
  created(){
        this.$router.push({
                path: "/",
              });
      }
    
}
</script>
<style>
.el-skeleton {
  float: left;
}
</style>
  