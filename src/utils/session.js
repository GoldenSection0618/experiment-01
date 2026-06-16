const USER_KEY = 'experiment2_user';

export function saveUser(user) {
  sessionStorage.setItem(USER_KEY, JSON.stringify(user));
}

export function getUser() {
  const value = sessionStorage.getItem(USER_KEY);
  return value ? JSON.parse(value) : null;
}

export function clearUser() {
  sessionStorage.removeItem(USER_KEY);
}
