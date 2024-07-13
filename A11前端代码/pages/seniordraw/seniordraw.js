3; // pages/seniordraw/seniordraw.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    showStyle: false,
    showLight: false,
    showCamera: false,
    showArtist: false,
    showSize: false,
    styleInfo: [
      "探索无限",
      "古风",
      "二次元",
      "写实风格",
      "浮世绘",
      "low poly ",
      "未来主义",
      "像素风格",
      "概念艺术",
      "赛博朋克",
      "洛丽塔风格",
      "巴洛克风格",
      "超现实主义",
      "水彩画",
      "蒸汽波艺术",
      "油画",
      "卡通画",
    ],
    lightInfo: [
      "特色照明",
      "背光灯",
      "黑光灯",
      "刺眼的光",
      "烛光",
      "音乐会灯光",
      "阳光直射",
      "黄昏",
      "火",
    ],
    cameraInfo: [
      "默认",
      "超分辨率图片",
      "高分辨率图片",
      "微距镜头",
      "针孔透镜",
      "第一人称视角",
      "广角镜头",
      "超广角镜头",
      "全景",
      "360全景",
    ],
    artistInfo: [
      "阿尔方斯·穆查斯",
      "安迪·沃霍尔",
      "小野洋子",
      "班克斯",
      "法兰西斯科·哥雅",
      "卡拉瓦乔",
      "大卫·霍克尼",
      "迭戈·里维拉",
      "埃德加·德加",
      "德拉克洛瓦",
      "弗朗西斯·培根",
      "弗里达·卡罗",
    ],
    sizeInfo: [],
    textVal: "",
    text: "",
    style: "",
    light: "",
    camera: "",
    artist: "",
    size: "",
    sizeId: "",
    version: "",
    file: "",
    num: 0,
    id: 0,
    changeDegree: 0,
    flag: false,
    sflag: true,
    versionInfo: ["v1(图片效果一般)", "v2(图片效果较好)"],
    taskId: "",
  },
  showStyle() {
    this.setData({ showStyle: true });
  },
  closeStyle() {
    this.setData({ showStyle: false });
  },
  showLight() {
    this.setData({ showLight: true });
  },
  closeLight() {
    this.setData({ showLight: false });
  },
  showCamera() {
    this.setData({ showCamera: true });
  },
  closeCamera() {
    this.setData({ showCamera: false });
  },
  showArtist() {
    this.setData({ showArtist: true });
  },
  closeArtist() {
    this.setData({ showArtist: false });
  },
  showSize() {
    this.setData({ showSize: true });
  },
  closeSize() {
    this.setData({ showSize: false });
  },
  copy() {
    var url = this.data.textVal;
    wx.setClipboardData({
      data: url,
      success: function (res) {},
    });
  },
  selectStyle(e) {
    console.log(e.currentTarget.dataset.index);
    var that = this;
    this.setData({
      showStyle: false,
      style: that.data.styleInfo[e.currentTarget.dataset.index],
      textVal:
        that.data.text +
        " " +
        that.data.styleInfo[e.currentTarget.dataset.index] +
        " " +
        that.data.light +
        " " +
        that.data.camera +
        " " +
        that.data.artist,
    });
  },
  selectLight(e) {
    console.log(e.currentTarget.dataset.index);
    var that = this;
    this.setData({
      showLight: false,
      light: that.data.lightInfo[e.currentTarget.dataset.index],
      textVal:
        that.data.text +
        " " +
        that.data.style +
        " " +
        that.data.lightInfo[e.currentTarget.dataset.index] +
        " " +
        that.data.camera +
        " " +
        that.data.artist,
    });
  },
  selectCamera(e) {
    console.log(e.currentTarget.dataset.index);
    var that = this;
    this.setData({
      showCamera: false,
      camera: that.data.cameraInfo[e.currentTarget.dataset.index],
      textVal:
        that.data.text +
        " " +
        that.data.style +
        " " +
        that.data.light +
        " " +
        that.data.cameraInfo[e.currentTarget.dataset.index] +
        " " +
        that.data.artist,
    });
  },
  selectArtist(e) {
    console.log(e.currentTarget.dataset.index);
    var that = this;
    this.setData({
      showArtist: false,
      artist: that.data.artistInfo[e.currentTarget.dataset.index],
      textVal:
        that.data.text +
        " " +
        that.data.style +
        " " +
        that.data.light +
        " " +
        that.data.camera +
        " " +
        that.data.artistInfo[e.currentTarget.dataset.index],
    });
  },
  selectSize(e) {
    console.log(e.currentTarget.dataset.index);
    var that = this;
    this.setData({
      id: e.currentTarget.dataset.index,
      showSize: false,
      sflag: true,
      size: that.data.sizeInfo[e.currentTarget.dataset.index].text,
      textVal:
        that.data.text +
        " " +
        that.data.style +
        " " +
        that.data.light +
        " " +
        that.data.camera +
        " " +
        that.data.artist,
    });
  },
  drwasText(e) {
    var that = this;
    this.setData({
      text: e.detail.value,
      textVal:
        e.detail.value +
        " " +
        that.data.style +
        " " +
        that.data.light +
        " " +
        that.data.camera,
    });
  },
  upload(e) {
    // console.log(e.detail.file.url);
    this.setData({ file: e.detail.file.url, flag: true });
  },
  selectVersion(e) {
    let id = e.currentTarget.dataset.index;
    var that = this;
    if (id == 1)
      that.setData({
        sizeInfo: [
          {
            text: "1:1(小)",
            width: 512,
            height: 512,
          },
          {
            text: "1:1(中)",
            width: 1024,
            height: 1024,
          },
          {
            text: "1:1(大)",
            width: 2048,
            height: 2048,
          },
          {
            text: "16:9(小)",
            width: 640,
            height: 360,
          },
          {
            text: "16:9(中)",
            width: 1280,
            height: 720,
          },
          {
            text: "16:9(大)",
            width: 2560,
            height: 1440,
          },
          {
            text: "9:16(小)",
            width: 360,
            height: 640,
          },

          {
            text: "9:16(中)",
            width: 720,
            height: 1280,
          },
          {
            text: "9:16(大)",
            width: 1440,
            height: 2560,
          },
        ],
        sflag: false,
        version: that.data.versionInfo[id].split("(")[0],
      });
    else
      that.setData({
        sizeInfo: [
          {
            text: "1:1(小)",
            width: 1024,
            height: 1024,
          },
          {
            text: "1:1(大)",
            width: 2048,
            height: 2048,
          },
          {
            text: "16:9(小)",
            width: 1280,
            height: 720,
          },
          {
            text: "16:9(大)",
            width: 2560,
            height: 1440,
          },
          {
            text: "9:16(小)",
            width: 720,
            height: 1280,
          },
          {
            text: "9:16(大)",
            width: 1440,
            height: 2560,
          },
        ],
        sflag: false,
        version: that.data.versionInfo[id].split("(")[0],
      });
  },
  changeNum(e) {
    console.log(e.detail);
    this.setData({
      num: e.detail,
    });
  },
  changeDeg(e) {
    console.log(e.detail);
    this.setData({
      changeDegree: e.detail,
    });
  },
  draw() {
    var that = this;
    wx.request({
      url: "http://www.elysialove.xyz:7701/api/ai_painting/advanced/text2image",
      data: {
        changeDegree: that.data.changeDegree,
        height: that.data.sizeInfo[that.data.id].height,
        imageNum: that.data.num,
        imageUrl: that.data.file,
        prompt: that.data.textVal,
        version: that.data.version,
        width: that.data.sizeInfo[that.data.id].width,
      },
      header: {
        "content-type": "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      method: "POST",
      dataType: "json",
      responseType: "text",
      success: (res) => {
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
          that.draw();
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
          // console.log(res);
        } else {
          console.log(res.data);
          //"1677589143592305632"
          that.setData({
            taskId: res.data.data.data.task_id,
          });
          wx.navigateTo({
            url:
              "/pages/seniorpanel/seniorpanel?taskId=" +
              that.data.taskId +
              "&prompt=" +
              that.data.text +
              "&subjectDescription=" +
              that.data.textVal +
              "&imageNum=" +
              that.data.num +
              "&changeDegree=" +
              that.data.changeDegree +
              "&width=" +
              that.data.sizeInfo[that.data.id].width +
              "&height=" +
              that.data.sizeInfo[that.data.id].height +
              "&imageUrl=" +
              that.data.file +
              "&version" +
              that.data.version,
          });
        }
      },
      fail: () => {},
      complete: () => {},
    });
  },
  draws() {
    var id = "1679161635353616554";
    wx.navigateTo({
      url:
        "/pages/seniorpanel/seniorpanel?taskId=" +
        id +
        "&prompt=白居易" +
        "&subjectDescription=白居易 写实风格 第一人称视角",
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {},

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
