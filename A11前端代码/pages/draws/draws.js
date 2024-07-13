// pages/draws/draws.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    imgUrl: "",
    taskId: 0,
    resInfo: {},
    style: "",
    text: "",
    createTime: "",
    code: "",
  },
  save() {
    var that = this;
    wx.downloadFile({
      url: that.data.imgUrl, //confirmationUrl图片地址
      success: (res) => {
        wx.saveImageToPhotosAlbum({
          filePath: res.tempFilePath,
          success: (res) => {
            wx.showToast({
              title: "保存成功",
              duration: 1000,
            });
          },
          fail: (err) => {
            wx.showToast({
              title: "获取权限失败",
              duration: 1000,
            });
          },
        });
      },
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // console.log(JSON.parse(options.taskId));
    var taskId = JSON.parse(options.taskId);
    var that = this;
    console.log(taskId);
    that.setData({ taskId: taskId });
    wx.showLoading({
      title: "绘制中",
    });
    let time = setInterval(() => {
      if (
        (that.data.imgUrl != "" && that.data.code == 200) ||
        that.data.code == 201
      ) {
        clearInterval(time);
        wx.hideLoading();
      } else {
        that.drawsImg();
      }
    }, 1000);
  },
  drawsImg() {
    var that = this;
    that
      .getImg({
        url:
          "http://www.elysialove.xyz:7701/api/ai_painting/getText2Image?taskId=" +
          that.data.taskId,
        data: {},
        header: {
          "content-type": "application/json",
          Authorization: wx.getStorageSync("token"),
          RefreshToken: wx.getStorageSync("refresh_token"),
        },
        method: "POST",
        dataType: "json",
        responseType: "text",
      })
      .then((res) => {
        let data = res.data;
        let code = data.code;
        if (code > 400) {
          let arr = data.message.split(";");
          let token = arr[0].substring(13);
          let refresh_token = arr[1].substring(14);
          console.log(token);
          console.log(refresh_token);
          wx.setStorage({
            key: "token",
            data: token,
          });
          wx.setStorage({
            key: "refresh_token",
            data: refresh_token,
          });
          that.drawsImg();
        } else if (code > 200 && code < 300) {
          wx.showToast({
            title: "请重新登录",
            icon: "error",
            duration: 2000, //持续的时间
          });
          setTimeout(() => {
            wx.navigateTo({
              url: "/pages/login/login",
            });
          }, 2100);
        } else {
          this.setData({
            imgUrl: res.data.data.data.img,
            style: res.data.data.data.style,
            text: res.data.data.data.text,
            createTime: res.data.data.data.createTime,
            code,
          });
        }
      });
  },
  getImg(options) {
    return new Promise((resolve, reject) => {
      // 逻辑：发送请求到服务器
      wx.request({
        url: options.url,
        method: options.method || "GET",
        data: options.data || {},
        header: options.header || {},
        success: (res) => {
          resolve(res);
        },
        fail: (err) => {
          reject(err);
        },
      });
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {},

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {},

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {},

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {},

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {},

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {},
});
