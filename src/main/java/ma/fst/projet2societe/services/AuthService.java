package ma.fst.projet2societe.services;

import ma.fst.projet2societe.dto.auth.ChangePasswordRequest;
import ma.fst.projet2societe.dto.auth.LoginRequest;
import ma.fst.projet2societe.dto.auth.LoginResponse;
import ma.fst.projet2societe.dto.auth.MeResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    MeResponse getMe(String login);

    void changePassword(String login, ChangePasswordRequest request);
}
