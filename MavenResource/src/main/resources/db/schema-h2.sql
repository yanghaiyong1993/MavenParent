create table SYS_USER
(
    ID        BIGINT auto_increment
        primary key,
    NAME      VARCHAR(30) default NULL,
    AGE       INT         default NULL,
    EMAIL     VARCHAR(50) default NULL,
    DELETED   INT         default 0 not null,
    MANAGE_ID BIGINT      default NULL
);

comment on column SYS_USER.ID is '主键ID';

comment on column SYS_USER.NAME is '姓名';

comment on column SYS_USER.AGE is '年龄';

comment on column SYS_USER.EMAIL is '邮箱';

comment on column SYS_USER.DELETED is '0未删除,1已删除';

comment on column SYS_USER.MANAGE_ID is '多租户id';

create table SYS_ADDRESS
(
    ID                 BIGINT auto_increment
        primary key,
    NAME               VARCHAR(30) default NULL,
    USER_ID            BIGINT      default NULL,
    CREATED_DATE       DATETIME    default NULL,
    LAST_MODIFIED_DATE DATETIME    default NULL,
    VERSION            INT         default 1 not null,
    MANAGE_ID          BIGINT      default NULL
);

comment on column SYS_ADDRESS.ID is '主键ID';

comment on column SYS_ADDRESS.NAME is '姓名';

comment on column SYS_ADDRESS.USER_ID is '用户id';

comment on column SYS_ADDRESS.CREATED_DATE is '创建时间';

comment on column SYS_ADDRESS.LAST_MODIFIED_DATE is '最后一次更新时间';

comment on column SYS_ADDRESS.VERSION is '版本锁';

comment on column SYS_ADDRESS.MANAGE_ID is '多租户id';

create table UMS_ADMIN
(
    ID          BIGINT auto_increment
        primary key,
    USERNAME    VARCHAR(64)  default NULL,
    PASSWORD    VARCHAR(64)  default NULL,
    ICON        VARCHAR(500) default NULL,
    EMAIL       VARCHAR(100) default NULL,
    NICK_NAME   VARCHAR(200) default NULL,
    NOTE        VARCHAR(500) default NULL,
    CREATE_TIME DATETIME     default NULL,
    LOGIN_TIME  DATETIME     default NULL,
    STATUS      INT          default '1'
);

comment on column UMS_ADMIN.ICON is '头像';

comment on column UMS_ADMIN.EMAIL is '邮箱';

comment on column UMS_ADMIN.NICK_NAME is '昵称';

comment on column UMS_ADMIN.NOTE is '备注信息';

comment on column UMS_ADMIN.CREATE_TIME is '创建时间';

comment on column UMS_ADMIN.LOGIN_TIME is '最后登录时间';

comment on column UMS_ADMIN.STATUS is '帐号启用状态：0->禁用；1->启用';

create table UMS_ADMIN_PERMISSION_RELATION
(
    ID            BIGINT auto_increment
        primary key,
    ADMIN_ID      BIGINT default NULL,
    PERMISSION_ID BIGINT default NULL,
    TYPE          INT    default NULL
);

create table UMS_ADMIN_ROLE_RELATION
(
    ID       BIGINT auto_increment
        primary key,
    ADMIN_ID BIGINT default NULL,
    ROLE_ID  BIGINT default NULL
);

create table UMS_MENU
(
    ID          BIGINT auto_increment
        primary key,
    PARENT_ID   BIGINT       default NULL,
    CREATE_TIME DATETIME     default NULL,
    TITLE       VARCHAR(100) default NULL,
    LEVEL       INT          default NULL,
    SORT        INT          default NULL,
    NAME        VARCHAR(100) default NULL,
    ICON        VARCHAR(200) default NULL,
    HIDDEN      INT          default NULL
);

comment on column UMS_MENU.PARENT_ID is '父级ID';

comment on column UMS_MENU.CREATE_TIME is '创建时间';

comment on column UMS_MENU.TITLE is '菜单名称';

comment on column UMS_MENU.LEVEL is '菜单级数';

comment on column UMS_MENU.SORT is '菜单排序';

comment on column UMS_MENU.NAME is '前端名称';

comment on column UMS_MENU.ICON is '前端图标';

comment on column UMS_MENU.HIDDEN is '前端隐藏';

create table UMS_PERMISSION
(
    ID          BIGINT auto_increment
        primary key,
    PID         BIGINT       default NULL,
    NAME        VARCHAR(100) default NULL,
    VALUE       VARCHAR(200) default NULL,
    ICON        VARCHAR(500) default NULL,
    TYPE        INT          default NULL,
    URI         VARCHAR(200) default NULL,
    STATUS      INT          default NULL,
    CREATE_TIME DATETIME     default NULL,
    SORT        INT          default NULL
);

comment on column UMS_PERMISSION.PID is '父级权限id';

comment on column UMS_PERMISSION.NAME is '名称';

comment on column UMS_PERMISSION.VALUE is '权限值';

comment on column UMS_PERMISSION.ICON is '图标';

comment on column UMS_PERMISSION.TYPE is '权限类型：0->目录；1->菜单；2->按钮（接口绑定权限）';

comment on column UMS_PERMISSION.URI is '前端资源路径';

comment on column UMS_PERMISSION.STATUS is '启用状态；0->禁用；1->启用';

comment on column UMS_PERMISSION.CREATE_TIME is '创建时间';

comment on column UMS_PERMISSION.SORT is '排序';

create table UMS_RESOURCE
(
    ID          BIGINT auto_increment
        primary key,
    CREATE_TIME DATETIME     default NULL,
    NAME        VARCHAR(200) default NULL,
    URL         VARCHAR(200) default NULL,
    DESCRIPTION VARCHAR(500) default NULL,
    CATEGORY_ID BIGINT       default NULL
);

comment on column UMS_RESOURCE.CREATE_TIME is '创建时间';

comment on column UMS_RESOURCE.NAME is '资源名称';

comment on column UMS_RESOURCE.URL is '资源URL';

comment on column UMS_RESOURCE.DESCRIPTION is '描述';

comment on column UMS_RESOURCE.CATEGORY_ID is '资源分类ID';

create table UMS_RESOURCE_CATEGORY
(
    ID          BIGINT auto_increment
        primary key,
    CREATE_TIME DATETIME     default NULL,
    NAME        VARCHAR(200) default NULL,
    SORT        INT          default NULL
);

comment on column UMS_RESOURCE_CATEGORY.CREATE_TIME is '创建时间';

comment on column UMS_RESOURCE_CATEGORY.NAME is '分类名称';

comment on column UMS_RESOURCE_CATEGORY.SORT is '排序';

create table UMS_ROLE
(
    ID          BIGINT auto_increment
        primary key,
    NAME        VARCHAR(100) default NULL,
    DESCRIPTION VARCHAR(500) default NULL,
    ADMIN_COUNT INT          default NULL,
    CREATE_TIME DATETIME     default NULL,
    STATUS      INT          default '1',
    SORT        INT          default '0'
);

comment on column UMS_ROLE.NAME is '名称';

comment on column UMS_ROLE.DESCRIPTION is '描述';

comment on column UMS_ROLE.ADMIN_COUNT is '后台用户数量';

comment on column UMS_ROLE.CREATE_TIME is '创建时间';

comment on column UMS_ROLE.STATUS is '启用状态：0->禁用；1->启用';

create table UMS_ROLE_MENU_RELATION
(
    ID      BIGINT auto_increment
        primary key,
    ROLE_ID BIGINT default NULL,
    MENU_ID BIGINT default NULL
);

comment on column UMS_ROLE_MENU_RELATION.ROLE_ID is '角色ID';

comment on column UMS_ROLE_MENU_RELATION.MENU_ID is '菜单ID';

create table UMS_ROLE_PERMISSION_RELATION
(
    ID            BIGINT auto_increment
        primary key,
    ROLE_ID       BIGINT default NULL,
    PERMISSION_ID BIGINT default NULL
);

create table UMS_ROLE_RESOURCE_RELATION
(
    ID          BIGINT auto_increment
        primary key,
    ROLE_ID     BIGINT default NULL,
    RESOURCE_ID BIGINT default NULL
);

comment on column UMS_ROLE_RESOURCE_RELATION.ROLE_ID is '角色ID';

comment on column UMS_ROLE_RESOURCE_RELATION.RESOURCE_ID is '资源ID';

create table UMS_COLUMN
(
    ID           BIGINT auto_increment,
    USE_TYPE     INT,
    TABLE_NAME   VARCHAR(255),
    TABLE_COLUMN VARCHAR(255),
    constraint UMS_COLUMN_PK
        primary key (ID)
);

comment on table UMS_COLUMN is '数据列';

comment on column UMS_COLUMN.ID is '主键id';

comment on column UMS_COLUMN.TABLE_NAME is '表名';

comment on column UMS_COLUMN.TABLE_COLUMN is '表列';

create table UMS_ROW
(
    ID         BIGINT auto_increment,
    USE_TYPE   INT,
    TABLE_NAME VARCHAR(255),
    COLUMN_ID  BIGINT,
    constraint UMS_ROW_PK
        primary key (ID)
);

comment on table UMS_ROW is '数据行';

create table UMS_ADMIN_GROUP
(
    ID        BIGINT auto_increment,
    NAME      VARCHAR(255),
    PARENT_ID BIGINT,
    LEFT_NUM  BIGINT,
    RIGHT_NUM BIGINT,
    constraint UMS_ADMIN_GROUP_PK
        primary key (ID)
);

comment on table UMS_ADMIN_GROUP is '用户组';

create table UMS_ROLE_ADMIN_GROUP_RELATION
(
    ID       BIGINT auto_increment,
    ROLE_ID  BIGINT,
    GROUP_ID BIGINT,
    constraint UMS_ROLE_ADMIN_GROUP_RELATION_PK
        primary key (ID)
);

comment on table UMS_ROLE_ADMIN_GROUP_RELATION is '角色与用户组关系';

create table UMS_ADMIN_ADMIN_GROUP_RELATION
(
    ID       BIGINT auto_increment,
    ADMIN_ID BIGINT,
    GROUP_ID BIGINT,
    constraint UMS_ADMINI_ADMIN_GROUP_RELATION_PK
        primary key (ID)
);

comment on table UMS_ADMIN_ADMIN_GROUP_RELATION is '用户与用户组关系';

create table UMS_ROLE_COLUMN_RELATION
(
    ID        BIGINT auto_increment,
    ROLE_ID   BIGINT,
    COLUMN_ID BIGINT,
    constraint UMS_ROLE_COLUMN_PK
        primary key (ID)
);

comment on table UMS_ROLE_COLUMN_RELATION is '角色与数据行';

create table UMS_ROLE_ROW_RELATION
(
    ID      BIGINT auto_increment,
    ROLE_ID BIGINT,
    ROW_ID  BIGINT,
    constraint UMS_ROLE_ROW_PK
        primary key (ID)
);

comment on table UMS_ROLE_ROW_RELATION is '角色与数据行关系';

