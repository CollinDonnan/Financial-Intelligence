export default function AbstractCard({ title, children }: { title: string, children: React.ReactNode }) {
  return (
    <section className="dashboard-card">
      <h2>{title}</h2>
      {children}
    </section>
  )
}