// pages/seniorpanel/seniorpanel.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    taskId: "",
    // 1679160682100152604
    imgUrl: "",
    width: "",
    height: "",
    conclusion: "",
    code: "",
    evaluation: 0,
    isUsePromptGen: 0,
    prompt: "",
    subjectDescription: "",
    textVal: "",
    show: false,
    imageNum: 0,
    version: "",
    imageUrl: "",
    changeDegree: 0,
    width: "",
    height: "",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // console.log(JSON.parse(options.taskId));
    var taskId = options.taskId;
    var that = this;
    console.log(taskId);
    that.setData({
      taskId: taskId,
      prompt: options.prompt,
      subjectDescription: options.subjectDescription,
      imageNum: options.imageNum,
      version: options.version,
      imageUrl: options.imageUrl,
      changeDegree: options.changeDegree,
      width: options.width,
      height: options.height,
    });
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
  changeEvaluation(event) {
    this.setData({
      evaluation: event.detail,
    });
  },
  changePrompt(event) {
    this.setData({
      isUsePromptGen: event.detail,
    });
  },
  appraise() {
    this.setData({ show: true });
  },
  drawsImg() {
    var that = this;
    that
      .getImg({
        url:
          "http://www.elysialove.xyz:7701/api/ai_painting/getText2ImageAdvanced?taskId=" +
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
        // console.log(res);
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
        } else if (code == 201) {
          wx.showToast({
            title: "请重新绘画",
            icon: "error",
            duration: 2000, //持续的时间
          });
          setTimeout(() => {
            wx.navigateBack({
              delta: 1,
            });
          }, 2100);
          console.log(res);
        } else {
          that.setData({
            imgUrl:
              res.data.data.data.sub_task_result_list[0].final_image_list[0]
                .img_url,
            width:
              res.data.data.data.sub_task_result_list[0].final_image_list[0]
                .width,
            height:
              res.data.data.data.sub_task_result_list[0].final_image_list[0]
                .height,
            conclusion:
              res.data.data.data.sub_task_result_list[0].final_image_list[0]
                .img_approve_conclusion,
            code,
          });

          console.log(res);
        }
      });
  },
  uploadImg() {
    var that = this;
    wx.request({
      url: "http://www.elysialove.xyz:7701/api/ai_painting/saveText2ImageAdvanced",
      data: {
        baiduUrl: that.data.imgUrl,
        changeDegree: that.data.changeDegree,
        height: that.data.height,
        imageNum: that.data.imageNum,
        imageUrl: that.data.imageUrl,
        prompt: that.data.text,
        version: that.data.version,
        width: that.data.width,
      },
      header: {
        "content-type": "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      method: "POST",
      dataType: "json",
      responseType: "text",
    }).then((res) => {
      // console.log(res);
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
      } else if (code == 201) {
        wx.showToast({
          title: "请重新绘画",
          icon: "error",
          duration: 2000, //持续的时间
        });
        setTimeout(() => {
          wx.navigateBack({
            delta: 1,
          });
        }, 2100);
        console.log(res);
      } else {
        console.log(res);
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
  drwasText(e) {
    this.setData({ textVal: e.detail.value });
  },
  sumbit() {
    var that = this;
    wx.request({
      url: "http://www.elysialove.xyz:7701/api/model/feedback/ai_painting/advanced",
      data: {
        evaluation: that.data.evaluation,
        isUsePromptGen: that.data.isUsePromptGen,
        prompt: that.data.prompt,
        review: that.data.textVal,
        subjectDescription: that.data.subjectDescription,
      },
      header: {
        "content-type": "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      method: "POST",
      dataType: "json",
      responseType: "text",
      success: (result) => {
        let data = result.data;
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
          that.sumbit();
        } else if (code == 201) {
          wx.showToast({
            title: "登录信息过期",
            icon: "error",
            duration: 2000, //持续的时间
          });
          setTimeout(() => {
            wx.navigateBack({
              delta: 1,
            });
          }, 2100);
          console.log(res);
        } else if (code == 200) {
          wx.showToast({
            title: "评价成功",
            icon: "success",
            image: "",
            duration: 200,
            mask: false,
            success: (result) => {
              that.setData({ show: false });
            },
            fail: () => {},
            complete: () => {},
          });
        }
        console.log(result.data.code);
      },
      fail: () => {},
      complete: () => {},
    });
  },
  cancel() {
    this.setData({ show: false });
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
