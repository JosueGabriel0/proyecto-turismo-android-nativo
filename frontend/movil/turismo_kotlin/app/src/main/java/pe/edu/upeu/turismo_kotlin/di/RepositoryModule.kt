package pe.edu.upeu.turismo_kotlin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pe.edu.upeu.turismo_kotlin.repository.CategoriaRepository
import pe.edu.upeu.turismo_kotlin.repository.CategoriaRepositoryImpl
import pe.edu.upeu.turismo_kotlin.repository.FileRepository
import pe.edu.upeu.turismo_kotlin.repository.FileRepositoryImpl
import pe.edu.upeu.turismo_kotlin.repository.LoginRepository
import pe.edu.upeu.turismo_kotlin.repository.LoginRepositoryImpl
import pe.edu.upeu.turismo_kotlin.repository.LugarTuristicoRepository
import pe.edu.upeu.turismo_kotlin.repository.LugarTuristicoRepositoryImpl
import pe.edu.upeu.turismo_kotlin.repository.RolRepository
import pe.edu.upeu.turismo_kotlin.repository.RolRepositoryImpl
import pe.edu.upeu.turismo_kotlin.repository.UsuarioRepository
import pe.edu.upeu.turismo_kotlin.repository.UsuarioRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun
            loginRepository(loginRepos: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    abstract fun
            rolRepository(rolRepos: RolRepositoryImpl): RolRepository

    @Binds
    @Singleton
    abstract fun
            usuarioRepository(usuarioRepos: UsuarioRepositoryImpl): UsuarioRepository

    @Binds
    @Singleton
    abstract fun
            fileRepository(fileRepos: FileRepositoryImpl): FileRepository

    @Binds
    @Singleton
    abstract fun
            lugarTuristicoRepository(lugarTuristicoRepos: LugarTuristicoRepositoryImpl): LugarTuristicoRepository

    @Binds
    @Singleton
    abstract fun
            categoriaRepository(categoriaRepos: CategoriaRepositoryImpl): CategoriaRepository

}
