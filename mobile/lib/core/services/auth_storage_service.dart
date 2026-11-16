import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class AuthStorageService {
  static const String _tokenKey = 'auth_token';
  static const String _userIdKey = 'user_id';
  static const String _roleKey = 'user_role';
  static const String _fullNameKey = 'user_full_name';
  static const String _emailKey = 'user_email';

  static const FlutterSecureStorage _storage =
      FlutterSecureStorage();

  Future<void> saveAuthData({
    required String token,
    required int userId,
    required String role,
    required String fullName,
    required String email,
  }) async {
    await _storage.write(
      key: _tokenKey,
      value: token,
    );

    await _storage.write(
      key: _userIdKey,
      value: userId.toString(),
    );

    await _storage.write(
      key: _roleKey,
      value: role,
    );

    await _storage.write(
      key: _fullNameKey,
      value: fullName,
    );

    await _storage.write(
      key: _emailKey,
      value: email,
    );
  }

  Future<String?> getToken() async {
    return _storage.read(key: _tokenKey);
  }

  Future<String?> getRole() async {
    return _storage.read(key: _roleKey);
  }

  Future<String?> getFullName() async {
    return _storage.read(key: _fullNameKey);
  }

  Future<String?> getEmail() async {
    return _storage.read(key: _emailKey);
  }

  Future<int?> getUserId() async {
    final value = await _storage.read(
      key: _userIdKey,
    );

    if (value == null) {
      return null;
    }

    return int.tryParse(value);
  }

  Future<bool> isLoggedIn() async {
    final token = await getToken();

    return token != null && token.isNotEmpty;
  }

  Future<void> clearAuthData() async {
    await _storage.deleteAll();
  }
}