// ===================================
// 🔑 AUTENTICACIÓN
// ===================================
const AuthAPI = {
  login: async (email, password) => {
    const url = `${API_URL}/auth/login`;
    return await doPost(url, { email, password });
  },

  register: async (name, email, password, role = "REPARTIDOR") => {
    const url = `${API_URL}/auth/register`;
    return await doPost(url, { name, email, password, role });
  },

  logout: async () => {
    const url = `${API_URL}/auth/logout`;
    return await doPost(url, {});
  },

  health: async () => {
    const url = `${API_URL}/auth/health`;
    return await doGet(url);
  },
};

// ===================================
// 👥 USUARIOS
// ===================================
const UserAPI = {
  list: async () => {
    const url = `${API_URL}/users`;
    return await doGet(url);
  },

  getById: async (id) => {
    const url = `${API_URL}/users/${id}`;
    return await doGet(url);
  },

  create: async (user) => {
    const url = `${API_URL}/users`;
    return await doPost(url, user);
  },

  update: async (id, user) => {
    const url = `${API_URL}/users/${id}`;
    return await doPut(url, user);
  },

  deleteUser: async (id) => {
    const url = `${API_URL}/users/${id}`;
    return await doDelete(url, {});
  },

  assignStore: async (userId, storeId) => {
    const url = `${API_URL}/users/${userId}/assign-store/${storeId}`;
    return await doPost(url, {});
  },

  saveFcmToken: async (userId, token) => {
    const url = `${API_URL}/users/${userId}/fcm-token`;
    return await doPost(url, token);
  },
};

// ===================================
// 📦 PRODUCTOS
// ===================================
const ProductAPI = {
  list: async () => {
    const url = `${API_URL}/products`;
    return await doGet(url);
  },

  getById: async (id) => {
    const url = `${API_URL}/products/${id}`;
    return await doGet(url);
  },

  create: async (product) => {
    const url = `${API_URL}/products`;
    return await doPost(url, product);
  },

  update: async (id, product) => {
    const url = `${API_URL}/products/${id}`;
    return await doPut(url, product);
  },

  deleteProduct: async (id) => {
    const url = `${API_URL}/products/${id}`;
    return await doDelete(url, {});
  },
};

// ===================================
// 🏪 TIENDAS
// ===================================
const StoreAPI = {
  list: async () => {
    const url = `${API_URL}/stores`;
    return await doGet(url);
  },

  getById: async (id) => {
    const url = `${API_URL}/stores/${id}`;
    return await doGet(url);
  },

  getByCode: async (code) => {
    const url = `${API_URL}/stores/by-code/${code}`;
    return await doGet(url);
  },

  create: async (store) => {
    const url = `${API_URL}/stores`;
    return await doPost(url, store);
  },

  update: async (id, store) => {
    const url = `${API_URL}/stores/${id}`;
    return await doPut(url, store);
  },

  deleteStore: async (id) => {
    const url = `${API_URL}/stores/${id}`;
    return await doDelete(url, {});
  },
};

// ===================================
// 📋 PEDIDOS
// ===================================
const OrderAPI = {
  list: async () => {
    const url = `${API_URL}/orders`;
    return await doGet(url);
  },

  getById: async (id) => {
    const url = `${API_URL}/orders/${id}`;
    return await doGet(url);
  },

  create: async (order) => {
    const url = `${API_URL}/orders`;
    return await doPost(url, order);
  },

  update: async (id, order) => {
    const url = `${API_URL}/orders/${id}`;
    return await doPut(url, order);
  },

  deleteOrder: async (id) => {
    const url = `${API_URL}/orders/${id}`;
    return await doDelete(url, {});
  },
};

// ===================================
// 📍 VISITAS
// ===================================
const VisitAPI = {
  list: async () => {
    const url = `${API_URL}/visits`;
    return await doGet(url);
  },

  registerScan: async (storeCode, repartidorId, lat, lng, hadOrder = false, temporary = false) => {
    const url = `${API_URL}/visits/scan?storeCode=${storeCode}&repartidorId=${repartidorId}&lat=${lat}&lng=${lng}&hadOrder=${hadOrder}&temporary=${temporary}`;
    return await doPost(url, {});
  },

  getByRepartidor: async (repartidorId) => {
    const url = `${API_URL}/visits/by-repartidor/${repartidorId}`;
    return await doGet(url);
  },
};

// ===================================
// 📅 ASIGNACIONES TEMPORALES
// ===================================
const TemporaryAssignmentAPI = {
  assign: async (storeId, repartidorId, date) => {
    const url = `${API_URL}/temporary-assignments?storeId=${storeId}&repartidorId=${repartidorId}&date=${date}`;
    return await doPost(url, {});
  },

  getByRepartidorAndDate: async (repartidorId, date) => {
    const url = `${API_URL}/temporary-assignments/repartidor/${repartidorId}/date/${date}`;
    return await doGet(url);
  },
};

// ===================================
// 🔔 NOTIFICACIONES
// ===================================
const NotificationAPI = {
  list: async () => {
    const url = `${API_URL}/notifications`;
    return await doGet(url);
  },

  getByType: async (type) => {
    const url = `${API_URL}/notifications?type=${type}`;
    return await doGet(url);
  },
};

