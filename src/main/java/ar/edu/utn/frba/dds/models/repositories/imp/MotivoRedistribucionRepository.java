package ar.edu.utn.frba.dds.models.repositories.imp;

import ar.edu.utn.frba.dds.models.domain.colaboraciones.utils.MotivoRedistribucionVianda;
import ar.edu.utn.frba.dds.models.repositories.IMotivoRedistribucionRepository;
import ar.edu.utn.frba.dds.serviceLocator.ServiceLocator;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;
import java.util.Optional;

public class MotivoRedistribucionRepository implements IMotivoRedistribucionRepository, WithSimplePersistenceUnit {


    public MotivoRedistribucionRepository() {
    }

    public static void main(String[] args) {
        MotivoRedistribucionVianda m = new MotivoRedistribucionVianda("otro");
        MotivoRedistribucionVianda m1 = new MotivoRedistribucionVianda("uno");
        MotivoRedistribucionVianda m2 = new MotivoRedistribucionVianda("hola");
        IMotivoRedistribucionRepository repositorio = ServiceLocator.get(IMotivoRedistribucionRepository.class);
        repositorio.guardar(m);
        repositorio.guardar(m1);
        repositorio.guardar(m2);

        repositorio.eliminar(m1);
        m2.setMotivo("lo cambio");
        repositorio.actualizar(m2);

        Optional<MotivoRedistribucionVianda> hidratado = repositorio.buscar(m.getId());
        //System.out.println(hidratado.get().getMotivo());
        Optional<MotivoRedistribucionVianda> hidratado2 = repositorio.buscar(m1.getId());

        List<MotivoRedistribucionVianda> lista = repositorio.buscarTodos();

    }

    @Override
    public Optional<MotivoRedistribucionVianda> buscar(String id) {
        return Optional.ofNullable(entityManager().find(MotivoRedistribucionVianda.class, id));
    }

    @Override
    public List<MotivoRedistribucionVianda> buscarTodos() {
        return entityManager().createQuery("from MotivoRedistribucionVianda where activo=:activo", MotivoRedistribucionVianda.class).
                setParameter("activo", true)
                .getResultList();
    }

    @Override
    public void guardar(MotivoRedistribucionVianda motivosRedistribucion) {
        withTransaction(() -> entityManager().persist(motivosRedistribucion));
    }

    public void guardar(MotivoRedistribucionVianda... motivosRedistribucion) {

        withTransaction(() -> {
            for (MotivoRedistribucionVianda motivo : motivosRedistribucion) {
                entityManager().persist(motivo);
            }
        });
    }

    @Override
    public void actualizar(MotivoRedistribucionVianda motivo) {
        withTransaction(() -> entityManager().merge(motivo));
    }

    @Override
    public void eliminar(MotivoRedistribucionVianda motivo) {
        withTransaction(() -> {
            motivo.borrarLogico();
            entityManager().merge(motivo);
        });
    }


}
