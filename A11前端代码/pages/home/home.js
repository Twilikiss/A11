// pages/home/home.js
const app = getApp();
Page({
  /**
   * 页面的初始数据
   */
  data: {
    userImg: "",
    name: "",
    sex: "",
  },
  loginOut() {
    app.globalData.userInfo = null;
    wx.showModal({
      title: "提示",
      content: "是否退出登录",
      success: function (res) {
        if (res.confirm) {
          //这里是点击了确定以后
          wx.request({
            url: "http://www.elysialove.xyz:7701/user/info/logout",
            data: {},
            header: {
              "Content-Type": "application/json",
              // accept: "application/json",
              Authorization: wx.getStorageSync("token"),
              RefreshToken: wx.getStorageSync("refresh_token"),
            },
            method: "GET",
            dataType: "json",
            responseType: "text",
            success: (result) => {
              if (result.data.code == 200) {
                wx.showToast({
                  title: result.data.data,
                  icon: "success",
                  duration: 1000, //持续的时间
                });
                wx.removeStorageSync("token");
                wx.removeStorageSync("refresh_token");
                setTimeout(() => {
                  wx.redirectTo({
                    url: "/pages/login/login",
                  });
                }, 1100);
              }
            },
            fail: () => {},
            complete: () => {},
          });
        } else {
        }
      },
    });
  },
  clearOut() {
    app.globalData.userInfo = null;
    wx.showModal({
      title: "提示",
      content: "是否退出登录",
      success: function (res) {
        if (res.confirm) {
          //这里是点击了确定以后
          wx.request({
            url: "http://www.elysialove.xyz:7701/user/info/history/ai_painting/text2image/all",
            data: {},
            header: {
              "Content-Type": "application/json",
              // accept: "application/json",
              Authorization: wx.getStorageSync("token"),
              RefreshToken: wx.getStorageSync("refresh_token"),
            },
            method: "GET",
            dataType: "json",
            responseType: "text",
            success: (result) => {
              console.log(result);
              // if (result.data.code == 200) {
              //   wx.showToast({
              //     title: result.data.data,
              //     icon: "success",
              //     duration: 1000, //持续的时间
              //   });
              //   wx.removeStorageSync("token");
              //   wx.removeStorageSync("refresh_token");
              //   // setTimeout(() => {
              //   //   wx.redirectTo({
              //   //     url: "/pages/login/login",
              //   //   });
              //   // }, 1100);
              // }
            },
            fail: () => {},
            complete: () => {},
          });
        } else {
        }
      },
    });
  },
  revise() {
    // console.log(123);
  },
  enquiries() {
    wx.navigateTo({
      url: "/pages/querydraw/querydraw",
    });
  },
  identify() {
    wx.navigateTo({
      url: "/pages/queryiden/queryiden",
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    console.log(app.globalData.userInfo);
    const sex = app.globalData.userInfo.gender == 0 ? "男" : "女";
    this.setData({
      userImg: app.globalData.userInfo.userIcon,
      name: app.globalData.userInfo.nickName,
      sex,
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
