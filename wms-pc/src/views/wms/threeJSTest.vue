<template>
    <div>
        <div id="label"></div>
        <div id="three" style="position: relative"></div>
    </div>
</template>

<script>
import * as THREE from "three";
import Stats from "three/examples/jsm/libs/stats.module";
import {OrbitControls} from "three/examples/jsm/controls/OrbitControls";
import {OutlinePass} from "three/examples/jsm/postprocessing/OutlinePass"
import {RenderPass} from "three/examples/jsm/postprocessing/RenderPass"
import {EffectComposer} from "three/examples/jsm/postprocessing/EffectComposer"
import {FontLoader} from "three/examples/jsm/loaders/FontLoader";
import {TextGeometry} from "three/examples/jsm/geometries/TextGeometry";
import {getList as getZoneList} from "@/api/wms/warehouse/zone";
import {getListFor3d} from "@/api/wms/basics/location";
import {page as getStock} from "@/api/wms/stock/stock"
import func from "@/util/func";
import Tenant from "@/views/core/tenant.vue";
import dat from "dat.gui/src/dat";

export default {
    name: "threeJSTest",
    components: {Tenant},
    data() {
        return {
            scene: undefined,
            camera: undefined,
            renderer: undefined,
            directionalLight: undefined,
            ambient: undefined,
            composer: undefined,
            outlinePass: undefined,
            RollTexture: undefined,
            mat: {
                planeMat: undefined,
                rackMat: undefined,
                rackMat2: undefined,
                cargoMat: undefined,
                lineMat: undefined,
                rollMat: undefined
            },
            floor: {
                height: 2000,       //地板长
                width: 2000,        //地板宽
                depth: 1,           //地板厚
            },
            wall: {
                width: 10,          //墙宽
                height: 200,        //强高
            },
            aisleWidth: 200,        // 过道长度
            zoneGeometryList: [],     // 手动创建的库区几何体
            shelfList: [],
            plane: {
                width: 50,              // 一个货架板在x轴上的长度
                height: 2,              // 一个货架板在y轴上的长度
                length: 80,             // 一个货架板在z轴上的长度
            },
            holder: {
                width: 2,              // 一个货架柱在x轴上的长度
                height: 40,              // 一个货架柱在y轴上的长度
                length: 2,             // 一个货架柱在z轴上的长度
            },
            cargoSpaceList: [], // 所有货位
            box: {
                size: 16,               // 货物的大小(立方体)
            },
            selectedObjects: [],
            zoneList: [],
            locationList: undefined,
            oneLocStockList: [],
        }
    },
    activated() {
        window.addEventListener('resize', this.onWindowResize, false);
        window.addEventListener('click', this.onMouseClick);
    },
    deactivated() {
        window.removeEventListener('resize', this.onWindowResize, false);
        window.removeEventListener('click', this.onMouseClick);
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.onWindowResize, false);
        window.removeEventListener('click', this.onMouseClick);
    },
    mounted() {
        this.initScene();       // 初始化场景
        this.initCamera();      // 初始化相机
        this.initRenderer();    // 初始化渲染器
        this.initLight();       // 初始化灯光
        this.initStats();       // 初始化性能插件
        this.initControls();    // 初始化控制器
        this.initCompose();
        this.initMat();         // 初始化材质信息

        this.creatWall();       // 创建墙体
        this.createFloor();     // 创建地板

        this.getData()          // 获取数据库数据
        this.createZone()       // 创建虚线框区域和库区名称

        this.animate();
    },
    methods: {
        initScene() {
            //创建场景
            this.scene = new THREE.Scene();
            this.scene.fog = new THREE.Fog(this.scene.background, 3000, 3000);
            // 添加坐标轴辅助器
            let axesHelper = new THREE.AxesHelper(1000);
            this.scene.add(axesHelper);
        },
        initCamera() {
            // 创建相机
            this.camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.1, 10000);
            // 设置相机位置
            this.camera.position.set(0, 50, 200);
        },
        initRenderer() {
            // 初始化渲染器
            this.renderer = new THREE.WebGLRenderer({
                antialias: true
            });
            // 设置渲染的尺寸和大小
            let threeWidth = getComputedStyle(document.getElementById("three")).width.slice(0, -2);
            this.renderer.setSize(threeWidth, window.innerHeight - 110);
            // 设置背景颜色，three.js渲染模型默认的背景颜色是黑色。 设置透明度
            this.renderer.setClearColor(0x4682B4, 1.0);
            document.getElementById("three").appendChild(this.renderer.domElement)
        },
        initLight() {
            // 模拟远处类似太阳的光源
            this.directionalLight = new THREE.DirectionalLight(0xffffff, 0.3);
            this.directionalLight.color.setHSL(0.1, 1, 0.95);
            this.directionalLight.position.set(0, 200, 0).normalize();
            this.scene.add(this.directionalLight);
            // AmbientLight,影响整个场景的光源
            this.ambient = new THREE.AmbientLight(0xffffff, 1);
            this.ambient.position.set(0, 0, 0);
            this.scene.add(this.ambient);
        },
        // 窗口变动触发的方法
        onWindowResize() {
            let width = getComputedStyle(document.getElementById("three")).width.slice(0, -2);
            let height = getComputedStyle(document.getElementById("three")).height.slice(0, -2);
            this.camera.aspect = width / height;
            this.camera.updateProjectionMatrix();
            this.renderer.setSize(width, height);
        },
        animate() {
            requestAnimationFrame(this.animate);
            // 使用渲染器，通过相机将场景渲染进来
            this.renderer.render(this.scene, this.camera);
            this.composer.render();
            this.update();
        },
        // 更新控件
        update() {
            this.stats.update();
            this.controls.update();
        },
        initStats() {
            this.stats = new Stats();
            this.stats.domElement.style.position = 'absolute';
            this.stats.domElement.style.left = '0px';
            this.stats.domElement.style.top = '0px';
            document.getElementById("three").appendChild(this.stats.domElement);
            return this.stats;
        },
        initControls() {
            this.controls = new OrbitControls(this.camera, this.renderer.domElement);
            // 使动画循环使用时阻尼或自转 意思是否有惯性
            this.controls.enableDamping = true;
            this.controls.dampingFactor = 0.5;
            // this.controls.autoRotate = true;    //自动旋转
            // 视角最小距离, 设置相机距离原点的最近距离
            this.controls.minDistance = 100;
            // 视角最远距离, 设置相机距离原点的最远距离
            this.controls.maxDistance = 3000;
            // 最大角度
            this.controls.maxPolarAngle = Math.PI / 2.2;

            this.controls.target = new THREE.Vector3(50, 50, 0);
        },
        initCompose() {
            this.composer = new EffectComposer(this.renderer);
            let renderPass = new RenderPass(this.scene, this.camera);
            this.composer.addPass(renderPass);
            this.outlinePass = new OutlinePass(new THREE.Vector2(window.innerWidth, window.innerHeight), this.scene, this.camera);
            this.outlinePass.edgeStrength = 5;//包围线浓度
            this.outlinePass.edgeGlow = 0.5;//边缘线范围
            this.outlinePass.edgeThickness = 2;//边缘线浓度
            this.outlinePass.pulsePeriod = 2;//包围线闪烁评率
            this.outlinePass.visibleEdgeColor.set('#ffffff');//包围线颜色
            this.outlinePass.hiddenEdgeColor.set('#190a05');//被遮挡的边界线颜色
            this.composer.addPass(this.outlinePass);
            this.raycaster = new THREE.Raycaster();
            this.mouse = new THREE.Vector2();
        },
        initMat() {
            this.mat.planeMat = new THREE.MeshLambertMaterial();
            this.mat.rackMat = new THREE.MeshLambertMaterial();
            this.mat.rackMat2 = new THREE.MeshPhongMaterial({color: 0x1C86EE});
            this.mat.cargoMat = new THREE.MeshLambertMaterial();
            this.mat.lineMat = new THREE.MeshLambertMaterial();
            this.mat.rollMat = new THREE.MeshLambertMaterial();
            let that = this;

            new THREE.TextureLoader().load('/img/3d/plane.png', function (map) {
                that.mat.planeMat.map = map;
                that.mat.planeMat.transparent = true;
                that.mat.planeMat.opacity = 1;
                that.mat.planeMat.needsUpdate = true;
            });
            new THREE.TextureLoader().load("/img/3d/rack.png", function (map) {
                that.mat.rackMat.map = map;
                that.mat.rackMat.needsUpdate = true;
            });
            new THREE.TextureLoader().load("/img/3d/box.png", function (map) {
                that.mat.cargoMat.map = map;
                that.mat.cargoMat.needsUpdate = true;
            });
            new THREE.TextureLoader().load("./img/3d/line.png", function (map) {
                that.mat.lineMat.map = map;
                that.mat.lineMat.needsUpdate = true;
            });
            this.rollTexture = new THREE.TextureLoader().load("./img/3d/biaoyu.png", function (map) {
                that.mat.rollMat.map = map;
                that.mat.rollMat.needsUpdate = true;
                that.mat.rollMat.transparent = true;
                that.mat.rollMat.side = THREE.DoubleSide;
            });
            this.rollTexture.wrapS = THREE.RepeatWrapping;
            this.rollTexture.wrapT = THREE.RepeatWrapping;
        },
        createFloor() {
            const that = this;
            let loader = new THREE.TextureLoader();
            loader.load("/img/3d/floor.jpg", function (texture) {
                // 贴图水平、竖直方向重复
                texture.wrapS = texture.wrapT = THREE.RepeatWrapping;
                // 重复次数
                texture.repeat.set(10, 10);
                // 创建物体
                const floorGeometry = new THREE.BoxGeometry(that.floor.width, that.floor.height, that.floor.depth);
                const floorMaterial = new THREE.MeshBasicMaterial({map: texture, side: THREE.DoubleSide});
                const floor = new THREE.Mesh(floorGeometry, floorMaterial);
                floor.position.y = -0.5;
                floor.rotation.x = Math.PI / 2;
                floor.name = "地面";
                that.scene.add(floor);
            });
        },
        creatWall() {
            this.createCubeWall(this.wall.width, this.wall.height, this.floor.height, 0,
                new THREE.MeshPhongMaterial({color: 0xafc0ca}), -this.floor.width / 2 - this.wall.width / 2, this.wall.height / 2 - 1, 0, "左墙面");
            this.createCubeWall(this.wall.width, this.wall.height, this.floor.height, 1,
                new THREE.MeshPhongMaterial({color: 0xafc0ca}), this.floor.width / 2 + this.wall.width / 2, this.wall.height / 2 - 1, 0, "右墙面");
            // this.createCubeWall(10, 200, 2600, 1.5, new THREE.MeshPhongMaterial({color: 0xafc0ca}), 0, 100, -1000, "墙面");
        },
        createCubeWall(width, height, depth, angle, material, x, y, z, name) {
            let cubeGeometry = new THREE.BoxGeometry(width, height, depth);
            let cube = new THREE.Mesh(cubeGeometry, material);
            cube.position.x = x;
            cube.position.y = y;
            cube.position.z = z;
            cube.rotation.y += angle * Math.PI;  //-逆时针旋转,+顺时针
            cube.name = name;
            this.scene.add(cube);
        },
        createZone() {
            let aisleWidth = this.floor.height / 10; // 过道长度
            this.aisleWidth = aisleWidth;
            const stage = {
                x: -this.floor.width / 2,
                y: 0,
                z: -this.floor.height / 2,
                width: aisleWidth,
                length: this.floor.height / 4
            }
            const pickTo = {
                x: -this.floor.width / 2,
                y: 0,
                z: this.floor.height / 4,
                width: aisleWidth,
                length: this.floor.height / 4
            }
            const rg = {
                x: -this.floor.width / 2,
                y: 0,
                z: 0,
                width: aisleWidth,
                length: this.floor.height / 2
            }
            let d = {
                x: stage.x + stage.width + aisleWidth,
                y: 0,
                z: 0,
                width: aisleWidth,
                length: this.floor.height / 2
            }
            let agv = {
                x: this.floor.width / 2,
                y: 0,
                z: 0,
                width: aisleWidth * 6,
                length: this.floor.height
            }
            this.addZone(stage.x + stage.width / 2, stage.y, stage.z + stage.length / 2, stage.width, stage.length, this.scene, "入库暂存区", "FF0000", 20, "左对齐");
            this.addZone(rg.x + rg.width / 2, rg.y, rg.z, rg.width, rg.length, this.scene, "人工拣货区", "FF0000", 20, "左对齐");
            this.addZone(pickTo.x + pickTo.width / 2, pickTo.y, pickTo.z + stage.length / 2, pickTo.width, pickTo.length, this.scene, "出库暂存区", "FF0000", 20, "左对齐");
            this.addZone(d.x + d.width / 2, d.y, d.z, d.width, d.length, this.scene, "D箱人工拣货区", "FF0000", 20, "左对齐");
            this.addZone(agv.x - agv.width / 2, agv.y, agv.z, agv.width, agv.length, this.scene, "AGV自动存储区", "FF0000", 20, "左对齐");
        },
        addZone(x, y, z, width, length, scene, name, textColor, fontSize, textposition) {
            let geometry = new THREE.PlaneGeometry(width, length);
            let zone = new THREE.Mesh(geometry, this.mat.planeMat);
            zone.position.set(x, y, z);
            zone.rotation.x = -Math.PI / 2.0;
            zone.name = name;
            scene.add(zone);
            this.zoneGeometryList.push(zone)
            new FontLoader().load('/json/FZYaoTi_Regular.json', function (font) {
                //加入立体文字
                let text = new TextGeometry(name, {
                    // 设定文字字体
                    font: font,
                    //尺寸
                    size: fontSize,
                    //厚度
                    height: 0.01
                });
                text.computeBoundingBox();
                //3D文字材质
                let m = new THREE.MeshStandardMaterial({color: "#" + textColor});
                let mesh = new THREE.Mesh(text, m)
                if (textposition === "左对齐") {
                    mesh.position.x = x - width / 2 + 10;
                } else if (textposition === "居中") {
                    mesh.position.x = x - 15;
                } else if (textposition === "右对齐") {
                    mesh.position.x = x + width / 2 - 60;
                }
                mesh.position.y = 1.3;
                mesh.position.z = z + length / 2 - 20;
                mesh.rotation.x = -Math.PI / 2.0;
                scene.add(mesh);
            });
        },
        async getData() {
            await getZoneList().then(res => {
                this.zoneList = res.data.data;
            });
            await getListFor3d().then(res => {
                this.locationList = res.data.data;
                this.createShelf(this.locationList);
            });
            await getStock({size: 99999999, current: 1}, {}).then((res) => {
                this.stockList = res.data.data.records;

                // 根据库位生成箱子，每个库位只有一个箱子
                let newVar = [...new Set(this.stockList.map(stock => stock.locCode))];
                newVar.forEach(locCode => {
                    this.addOneCargos(locCode)
                })
            })
        },
        //  ************************************ 根据货架配置创建货架并添加到库区中 start ************************************
        createShelf(locationList) {
            //第一排 人工区拣货区和对应库位， 库区几何对象
            let bank1LocList = locationList.filter(x => Number(x.locBank) === 1);
            let rgLocationGeometry = this.zoneGeometryList.find(zone => zone.name === "人工拣货区")
            let bank1MaxLocColumn = Math.max.apply(Math, bank1LocList.map(item => item.locColumn))
            let firstLocPositionZ = rgLocationGeometry.position.z + (bank1MaxLocColumn * this.plane.length) / 2
            let firstLocPositionX = rgLocationGeometry.position.x
            let firstLocPositionY = this.holder.height;

            for (let i = 0; i < bank1LocList.length; i++) {
                // 货板
                let plane = new THREE.BoxGeometry(this.plane.width, this.plane.height, this.plane.length);
                let obj = new THREE.Mesh(plane, this.mat.rackMat);
                let planePositionX = firstLocPositionX;
                let planePositionY = firstLocPositionY * Number(bank1LocList[i].locLevel);
                let planePositionZ = firstLocPositionZ - this.plane.length * Number(locationList[i].locColumn);
                obj.position.set(planePositionX, planePositionY, planePositionZ);
                obj.name = bank1LocList[i].locCode;
                this.scene.add(obj);

                let cargoSpaceObj = {
                    location: bank1LocList[i],
                    positionX: firstLocPositionX,
                    positionY: planePositionY,
                    positionZ: planePositionZ
                }
                this.cargoSpaceList.push(cargoSpaceObj);

                // 柱子
                let holder = new THREE.BoxGeometry(this.holder.width, this.holder.height, this.holder.length);
                let holderLeft = new THREE.Mesh(holder, this.mat.rackMat2);
                let holderRight = new THREE.Mesh(holder, this.mat.rackMat2);
                let holderPositionZ;
                if (Number(bank1LocList[i].locColumn) === 1) {
                    holderPositionZ = firstLocPositionZ - this.plane.length / 2;
                } else {
                    holderPositionZ = (firstLocPositionZ - this.plane.length / 2) - (this.plane.length * (Number(bank1LocList[i].locColumn - 1)));
                }

                let holderPositionY = this.holder.height * Number(bank1LocList[i].locLevel) - this.holder.height / 2;
                let holderLeftPositionX = firstLocPositionX - this.plane.width / 2 + this.holder.width / 2
                let holderRightPositionX = firstLocPositionX + this.plane.width / 2 - this.holder.width / 2
                holderLeft.position.set(holderLeftPositionX, holderPositionY, holderPositionZ);
                holderRight.position.set(holderRightPositionX, holderPositionY, holderPositionZ);
                this.scene.add(holderLeft);
                this.scene.add(holderRight);

                if (Number(bank1LocList[i].locColumn) === bank1MaxLocColumn || Number(bank1LocList[i].locColumn) === 4) {
                    let obj1 = new THREE.Mesh(holder, this.mat.rackMat2);
                    let obj2 = new THREE.Mesh(holder, this.mat.rackMat2);
                    holderPositionZ = firstLocPositionZ - (this.plane.length * (Number(bank1LocList[i].locColumn))) - this.plane.length / 2;
                    obj1.position.set(holderLeftPositionX, holderPositionY, holderPositionZ);
                    obj2.position.set(holderRightPositionX, holderPositionY, holderPositionZ);
                    this.scene.add(obj1);
                    this.scene.add(obj2);
                }
            }
            //二排 D箱拣货区、D箱自动存储区、入、出库接驳区
            let bank2LocList = locationList.filter(x => Number(x.locBank) === 2);
            let bank2LocGeometry = this.zoneGeometryList.find(zone => zone.name === "D箱人工拣货区")
            let bank2MaxLocColumn = Math.max.apply(Math, bank2LocList.map(item => item.locColumn))
            let bank2LocPositionZ = bank2LocGeometry.position.z + (bank2MaxLocColumn * this.plane.length) / 2
            let bank2LocPositionX = bank2LocGeometry.position.x

            let bank2LocPositionY = this.holder.height;
            for (let j = 0; j < bank2LocList.length; j++) {
                // 货板
                let planeLength = this.plane.length / 2;
                let multiplePlaneLength
                if (bank2LocList[j].lpnTypeCode === 'A') {
                    planeLength = planeLength * 2;
                    multiplePlaneLength = planeLength * Number(bank2LocList[j].locColumn) - planeLength / 4;
                } else {
                    if (bank2LocList[j].locCode.indexOf("R") !== -1 || bank2LocList[j].locCode.indexOf("P") !== -1) {
                        multiplePlaneLength = planeLength * Number(bank2LocList[j].locCode.substring(9, 11));
                    } else {
                        multiplePlaneLength = planeLength * Number(bank2LocList[j].locCode.substring(7, 9));
                    }
                }

                let plane = new THREE.BoxGeometry(this.plane.width, this.plane.height, planeLength);
                let obj = new THREE.Mesh(plane, this.mat.rackMat);
                let planePositionX = bank2LocPositionX;
                let planePositionY = bank2LocPositionY * Number(bank2LocList[j].locLevel);
                let planePositionZ = bank2LocPositionZ - multiplePlaneLength;
                obj.position.set(planePositionX, planePositionY, planePositionZ);
                obj.name = bank2LocList[j].locCode;
                this.scene.add(obj);

                let cargoSpaceObj = {
                    location: bank2LocList[j],
                    positionX: bank2LocPositionX,
                    positionY: planePositionY,
                    positionZ: planePositionZ
                }
                this.cargoSpaceList.push(cargoSpaceObj);

                // 柱子
                let holder = new THREE.BoxGeometry(this.holder.width, this.holder.height, this.holder.length);
                let holderLeft = new THREE.Mesh(holder, this.mat.rackMat2);
                let holderRight = new THREE.Mesh(holder, this.mat.rackMat2);
                let holderPositionZ;
                if (Number(bank2LocList[j].locColumn) === 1) {
                    holderPositionZ = bank2LocPositionZ - planeLength / 4;
                } else {
                    if (bank2LocList[j].lpnTypeCode === 'A') {
                        holderPositionZ = bank2LocPositionZ - planeLength / 4 - (this.plane.length * (Number(bank2LocList[j].locColumn - 1)));
                    } else {
                        holderPositionZ = bank2LocPositionZ - planeLength / 2 - (this.plane.length * (Number(bank2LocList[j].locColumn - 1)));
                    }
                }

                let holderPositionY = this.holder.height * Number(bank2LocList[j].locLevel) - this.holder.height / 2;
                let holderLeftPositionX = bank2LocPositionX - this.plane.width / 2 + this.holder.width / 2
                let holderRightPositionX = bank2LocPositionX + this.plane.width / 2 - this.holder.width / 2
                holderLeft.position.set(holderLeftPositionX, holderPositionY, holderPositionZ);
                holderRight.position.set(holderRightPositionX, holderPositionY, holderPositionZ);
                this.scene.add(holderLeft);
                this.scene.add(holderRight);

                // 在消防通道处和末尾添加柱子
                if (((Number(bank2LocList[j].locLevel) === 1 || Number(bank2LocList[j].locLevel) === 2)&& (Number(bank2LocList[j].locCode.substring(7, 9)) === 14))
                    || Number(bank2LocList[j].locCode.substring(7, 9)) === 34 || Number(bank2LocList[j].locCode.substring(9, 11)) === 34
                    || Number(bank2LocList[j].locCode.substring(7, 9)) === 28 || Number(bank2LocList[j].locCode.substring(9, 11)) === 28
                    ) {
                    let obj1 = new THREE.Mesh(holder, this.mat.rackMat2);
                    let obj2 = new THREE.Mesh(holder, this.mat.rackMat2);
                    holderPositionY = this.holder.height * Number(bank2LocList[j].locLevel) - this.holder.height / 2;
                    if (Number(bank2LocList[j].locCode.substring(9, 11)) === 28 || Number(bank2LocList[j].locCode.substring(9, 11)) === 34) {
                        holderPositionZ = bank2LocPositionZ - planeLength * (Number(bank2LocList[j].locCode.substring(9, 11))) - planeLength / 2;
                    }else if (Number(bank2LocList[j].locCode.substring(7, 9)) === 28 || Number(bank2LocList[j].locCode.substring(7, 9)) === 28){
                        holderPositionZ = bank2LocPositionZ - planeLength * (Number(bank2LocList[j].locCode.substring(7, 9))) - planeLength / 2;

                    } else{
                        holderPositionZ = bank2LocPositionZ - (planeLength * (Number(bank2LocList[j].locCode.substring(7, 9)))) - planeLength / 2;
                    }
                    obj1.position.set(holderLeftPositionX, holderPositionY, holderPositionZ);
                    obj2.position.set(holderRightPositionX, holderPositionY, holderPositionZ);
                    this.scene.add(obj1);
                    this.scene.add(obj2);
                }
            }


            //第三四五六七排 自动区
            let agvLocationGeometry = this.zoneGeometryList.find(zone => zone.name === "AGV自动存储区")
            for (let i = 3; i <= 7; i++) {
                let bank34567LocList = locationList.filter(x => Number(x.locBank) === i);
                let bank34567MaxLocColumn = Math.max.apply(Math, bank34567LocList.map(item => item.locColumn))
                let bank34567LocPositionZ = agvLocationGeometry.position.z + (bank34567MaxLocColumn * this.plane.length) / 2
                let bank34567LocPositionX;
                if (i === 3 || i === 4) {
                    bank34567LocPositionX = agvLocationGeometry.position.x - agvLocationGeometry.geometry.parameters.width / 2 + (this.plane.width * i)
                } else if (i === 5 || i === 6) {
                    bank34567LocPositionX = agvLocationGeometry.position.x - agvLocationGeometry.geometry.parameters.width / 2 + (this.plane.width * i) + this.aisleWidth
                } else {
                    bank34567LocPositionX = agvLocationGeometry.position.x - agvLocationGeometry.geometry.parameters.width / 2 + (this.plane.width * i) + this.aisleWidth * 2
                }
                let bank34567LocPositionY = this.holder.height;
                for (let j = 0; j < bank34567LocList.length; j++) {
                    // 货板
                    let planeLength = this.plane.length / 2;
                    let multiplePlaneLength
                    if (bank34567LocList[j].lpnTypeCode === 'A') {
                        planeLength = planeLength * 2;
                        multiplePlaneLength = planeLength * Number(bank34567LocList[j].locColumn) - planeLength / 4;
                    } else {
                        multiplePlaneLength = planeLength * Number(bank34567LocList[j].locCode.substring(7, 9));
                    }
                    let plane = new THREE.BoxGeometry(this.plane.width, this.plane.height, planeLength);
                    let obj = new THREE.Mesh(plane, this.mat.rackMat);
                    let planePositionX = bank34567LocPositionX;
                    let planePositionY = bank34567LocPositionY * Number(bank34567LocList[j].locLevel);
                    let planePositionZ = bank34567LocPositionZ - multiplePlaneLength;

                    obj.position.set(planePositionX, planePositionY, planePositionZ);
                    obj.name = bank34567LocList[j].locCode;
                    this.scene.add(obj);

                    let cargoSpaceObj = {
                        location: bank34567LocList[j],
                        positionX: bank34567LocPositionX,
                        positionY: planePositionY,
                        positionZ: planePositionZ
                    }
                    this.cargoSpaceList.push(cargoSpaceObj);

                    // 柱子
                    let holder = new THREE.BoxGeometry(this.holder.width, this.holder.height, this.holder.length);
                    let holderLeft = new THREE.Mesh(holder, this.mat.rackMat2);
                    let holderRight = new THREE.Mesh(holder, this.mat.rackMat2);
                    let holderPositionZ;
                    if (Number(bank34567LocList[j].locColumn) === 1) {
                        holderPositionZ = bank34567LocPositionZ - planeLength / 2;
                    } else {
                        if (bank34567LocList[j].lpnTypeCode === 'A') {
                            holderPositionZ = bank34567LocPositionZ - planeLength / 4 - (this.plane.length * (Number(bank34567LocList[j].locColumn - 1)));
                        } else {
                            holderPositionZ = bank34567LocPositionZ - planeLength / 2 - (this.plane.length * (Number(bank34567LocList[j].locColumn - 1)));
                        }
                    }

                    let holderPositionY = this.holder.height * Number(bank34567LocList[j].locLevel) - this.holder.height / 2;
                    let holderLeftPositionX = bank34567LocPositionX - this.plane.width / 2 + this.holder.width / 2
                    let holderRightPositionX = bank34567LocPositionX + this.plane.width / 2 - this.holder.width / 2
                    holderLeft.position.set(holderLeftPositionX, holderPositionY, holderPositionZ);
                    holderRight.position.set(holderRightPositionX, holderPositionY, holderPositionZ);
                    this.scene.add(holderLeft);
                    this.scene.add(holderRight);

                    // 在消防通道处添加柱子
                    if (((Number(bank34567LocList[j].locLevel) === 1 || Number(bank34567LocList[j].locLevel) === 2)
                            && (Number(bank34567LocList[j].locCode.substring(7, 9)) === 14)) || Number(bank34567LocList[j].locCode.substring(7, 9)) === 34
                        || (bank34567LocList[j].lpnTypeCode === 'A' && Number(bank34567LocList[j].locCode.substring(7, 9)) === 33)) {
                        let obj1 = new THREE.Mesh(holder, this.mat.rackMat2);
                        let obj2 = new THREE.Mesh(holder, this.mat.rackMat2);
                        holderPositionY = this.holder.height * Number(bank34567LocList[j].locLevel) - this.holder.height / 2;
                        if (bank34567LocList[j].lpnTypeCode === 'A' && Number(bank34567LocList[j].locCode.substring(7, 9)) === 33) {
                            holderPositionZ = bank34567LocPositionZ - planeLength * (Number(bank34567LocList[j].locColumn)) - planeLength / 4;
                        } else {
                            holderPositionZ = bank34567LocPositionZ - (planeLength * (Number(bank34567LocList[j].locCode.substring(7, 9)))) - planeLength / 2;
                        }
                        obj1.position.set(holderLeftPositionX, holderPositionY, holderPositionZ);
                        obj2.position.set(holderRightPositionX, holderPositionY, holderPositionZ);
                        this.scene.add(obj1);
                        this.scene.add(obj2);
                    }
                }
            }
        },
        //  ************************************ 根据货架配置添加货架 end ************************************
        //  ************************************ 添加物品 start ************************************
        addOneCargos(locCode) {
            let cargoSpaceList = this.cargoSpaceList;
            let cargoSpace = cargoSpaceList.find(x => x.location.locCode === locCode);
            if (func.isNotEmpty(cargoSpace)) {
                let x = cargoSpace.positionX;
                let y = cargoSpace.positionY + this.box.size / 2;
                let z = cargoSpace.positionZ;
                this.addCargo(x, y, z, this.box.size, this.box.size, this.box.size, locCode);
            }
        },
        addCargo(x, y, z, box_x, box_y, box_z, locCode) {
            let geometry = new THREE.BoxGeometry(box_x, box_y, box_z);
            let obj = new THREE.Mesh(geometry, this.mat.cargoMat);
            obj.position.set(x, y, z);
            obj.locCode = locCode;
            obj.type = "cargo"
            this.scene.add(obj);
        },
        //  ************************************ 添加物品 end ************************************

        // 鼠标单击事件
        onMouseClick(event) {
            let x, y;
            // 屏幕坐标转标准设备坐标
            // getBoundingClientRectangle会返回当前元素的视口大小.
            x = ((event.clientX - document.getElementById("three").getBoundingClientRect().left) / document.getElementById("three").offsetWidth) * 2 - 1;// 标准设备横坐标
            y = -((event.clientY - document.getElementById("three").getBoundingClientRect().top) / document.getElementById("three").offsetHeight) * 2 + 1;// 标准设备纵坐标
            // }
            let standardVector = new THREE.Vector3(x, y, 1);// 标准设备坐标
            // 标准设备坐标转世界坐标
            let worldVector = standardVector.unproject(this.camera);
            // 射线投射方向单位向量(worldVector坐标减相机位置坐标)
            let ray = worldVector.sub(this.camera.position).normalize();
            // 创建射线投射器对象
            let rayCaster = new THREE.Raycaster(this.camera.position, ray);
            // 返回射线选中的对象 第二个参数如果不填 默认是false
            let intersects = rayCaster.intersectObjects([this.scene], true);
            this.raycaster.setFromCamera(this.mouse, this.camera);

            if (func.isEmpty(intersects)) {
                document.getElementById("label").setAttribute("style", "display:none;");//隐藏说明性标签
                return;
            }
            if (intersects[0].object.name === "地面" ||
                intersects[0].object.name === "" ||
                intersects[0].object.name === "左墙面" ||
                intersects[0].object.name === "右墙面") {
                document.getElementById("label").setAttribute("style", "display:none;"); // 隐藏说明性标签
            } else {
                document.getElementById("label").setAttribute("style", "display:block;");// 显示说明性标签
                document.getElementById("label").style.left = x;    // 修改标签的位置
                document.getElementById("label").style.top = `${y - 40}`;    // 修改标签的位置
                document.getElementById("label").innerText = intersects[0].object.name;// 显示模型信息
            }

            let stockMsgDom = document.getElementsByClassName("dg main a");

            if (stockMsgDom.length !== 0) {
                for (let i = stockMsgDom.length - 1; i >= 0; i--) {
                    stockMsgDom[i].remove();
                }
            }

            if (intersects[0].object.type === "cargo") {
                document.getElementById("label").setAttribute("style", "display:none;"); // 隐藏说明性标签
                let locCode = intersects[0].object.locCode
                let oneLocStockList = this.stockList.filter(x => x.locCode === locCode);
                this.initGui(oneLocStockList);
                this.oneLocStockList.selectObj = intersects[0].object;
            }
            this.selectedObjects.push(intersects[0].object);
            this.outlinePass.selectedObjects = this.selectedObjects;//给选中的线条和物体加发光特效

            this.selectedObjects = [];
        },
        //  ************************************ 添加侧边栏信息 start ************************************
        initGui(oneLocStockList) {
            let i = 0;
            oneLocStockList.forEach(stock => {
                let gui = new dat.GUI();
                document.getElementById("three").appendChild(gui.domElement);
                gui.domElement.style = `position:absolute;top:0px;right:${i}px;height:600px;z-index:99999;margin-right:0px`;
                stock.stockBalance = stock.stockBalance.toString();
                gui.add(stock, 'boxCode').name("箱码：");
                gui.add(stock, 'skuCode',).name("物品编码：");
                gui.add(stock, 'skuName').name("物品名称：");
                gui.add(stock, 'stockBalance').name("数量：");

                i += 245;
            })
        },
        //  ************************************ 添加侧边栏信息 end ************************************
    }

}

</script>

<style scoped>
#label {
    position: absolute;
    padding: 10px;
    background: rgba(255, 255, 255, 0.6);
    line-height: 1;
    border-radius: 5px;
    display: none;
    z-index: 99999;
}
</style>
